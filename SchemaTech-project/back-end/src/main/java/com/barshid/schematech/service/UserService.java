package com.barshid.schematech.service;

import com.barshid.schematech.controller.dto.MediaTO;
import com.barshid.schematech.controller.dto.PublicUser;
import com.barshid.schematech.controller.dto.UserModifyTO;
import com.barshid.schematech.controller.dto.UserTO;
import com.barshid.schematech.controller.pagebean.NewPassPage;
import com.barshid.schematech.model.bo.*;
import com.barshid.schematech.model.repository.MediaRepository;
import com.barshid.schematech.model.repository.ResetPasswordRepository;
import com.barshid.schematech.model.repository.UserGroupRepository;
import com.barshid.schematech.model.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;


@Slf4j
@Service
@Transactional
public class UserService {
    UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User Save(User u) {
        return userRepository.save(u);
    }

    private UserGroupRepository userGroupRepository;

    @Autowired
    public void setUserGroupRepository(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    @Autowired
    private Environment env;

    @Autowired
    public PasswordEncoder passwordEncoder;

    //    @SneakyThrows
    public String register(UserModifyTO userModifyTO) throws ResponseStatusException {
//        try {
        User user = new User();
        BeanUtils.copyProperties(userModifyTO, user);
        List<UserGroup> lug = userGroupRepository.findUserGroupByName("usergroup1");
        if (!lug.isEmpty()) {
            user.getGroups().addAll(lug);
        }
//        String[] str = userModifyTO.getFullName().split(" ");
//        user.setFirstName(str[0]);
//        user.setLastName(userModifyTO.getFullName().substring(str[0].length()));
        if (user.getPhoneNumber() == null || user.getPhoneNumber() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Please fill the phone number.");
        }
        if (user.getCountryCode() == null || user.getCountryCode() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Please fill country code");
        }
        if (userRepository.countUserEmail(user.getEmail())>0) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "email already used.");
        if (userRepository.countUserPhoneNumber(user.getCountryCode().toString()+user.getPhoneNumber().toString())>0)
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "phone number already used.");
//        if(user.getPhoneNumber().isEmpty() && user.getEmail().contains("@")){
//            user.setUsername(user.getEmail());
//        }else if (!user.getPhoneNumber().isEmpty()){
//            user.setUsername(user.getPhoneNumber());
//        }
//        else{//send error.
//            return new Error(0,"unexpected error").toString();
//            throw new projectExpception(HttpStatus.NOT_ACCEPTABLE, "unexpected error");
//        }
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(userModifyTO.getPassword()));
        user.setRegisterTime(new Timestamp(System.currentTimeMillis()));
        //send sms Security code 5 digit.
        int m = (int) Math.pow(10, 5 - 1);
        int digitCode = m + new Random().nextInt(9 * m);
        log.info("+" + user.getCountryCode() + user.getPhoneNumber() + " schematek registry code: " + digitCode);
        user.setSmspass(passwordEncoder.encode(String.valueOf(digitCode)));
        userRepository.save(user);
//        Message message = Message
//                .creator( new PhoneNumber("+"+user.getCountryCode()+user.getPhoneNumber()), // to
//                        new PhoneNumber(env.getProperty("schematek.twilio.from")), // from
//                        "your schematek registry code: "+digitCode)
//                .create();
//        log.info("message send id:"+message.getSid()+" register user:"+user.getCountryCode()+user.getPhoneNumber());
        return "";

    }

    public User getUserByEmail(String e) {
        List<User> ul = this.userRepository.findUserByEmail(e);
        return !ul.isEmpty() ? ul.get(0) : null;
    }

    public User getUserByPhoneNumber(String phone) {
        List<User> ul = this.userRepository.findUserByPhoneNumber(phone);
        return !ul.isEmpty() ? ul.get(0) : null;
    }

    public PublicUser findUserById(Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (!optionalUser.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found.");
        User u = (User) optionalUser.get();
//        UserTO uTO = new UserTO();
//        BeanUtils.copyProperties( u,uTO);
//        BeanUtils.copyProperties( u.getAvatar(),uTO.getAvatar());
        return new PublicUser(u, env.getProperty("schematech.s3.bucketname"), cdnService);
    }


    public UserTO findUserByToken(String username) throws ResponseStatusException {
        User u = getUserByPhoneNumber(username);
        return new UserTO(u, env.getProperty("schematek.s3.bucketname"), cdnService);
    }

    public void updateUserByToken(String username, UserModifyTO userModifyTO) throws ResponseStatusException {
        log.debug("userToken name is :" + username);
        if (userModifyTO.getPhoneNumber() == null || userModifyTO.getPhoneNumber() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "invalid phoneNumber! ");
        }
        if (userModifyTO.getCountryCode() == null || userModifyTO.getCountryCode() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "invalid country code!");
        }
        User user = getUserByPhoneNumber(username);
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login user not found!");

        if (!userModifyTO.getEmail().equals(user.getEmail()) && userRepository.countUserEmail(userModifyTO.getEmail()) > 0)
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This email is already in use!");

        String phoneEntery = userModifyTO.getCountryCode().toString() + userModifyTO.getPhoneNumber().toString();
        if (!phoneEntery.equals(user.getCountryCode().toString() + user.getPhoneNumber().toString())
                && userRepository.countUserPhoneNumber(userModifyTO.getCountryCode().toString() + userModifyTO.getPhoneNumber().toString()) > 0)
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This phone number is already in use!");

        //Run issue...
        BeanUtils.copyProperties(userModifyTO, user, "password");
//        String[] str = userModifyTO.getFullName().split(" ");
//        user.setFirstName(str[0]);
//        user.setLastName(userModifyTO.getFullName().substring(str[0].length()));
////        user.setPassword(passwordEncoder.encode(userModifyTO.getPassword()));
        if (userModifyTO.getTherapistId() != null) {
            Optional<User> optionalTherap = userRepository.findById(userModifyTO.getTherapistId());
            if (optionalTherap.isPresent() && optionalTherap.get().getIsTherapist())
                user.setTherapist(optionalTherap.get());
        }
        userRepository.save(user);
    }

    public void sendVerificationCode(Long phoneNumber, Long countryCode) {
        log.debug(">>>+" + countryCode + " " + phoneNumber + " ...");

        //send sms Security code 5 digit.
        int m = (int) Math.pow(10, 5 - 1);
        int digitCode = m + new Random().nextInt(9 * m);
        List<User> users = userRepository.findUserByPhoneNumber(countryCode.toString() + phoneNumber.toString());
        if (users != null && users.size() >= 1) {
            User user = users.get(0);
            user.setSmspass(passwordEncoder.encode(String.valueOf(digitCode)));
            userRepository.save(user);
//            Message message = Message
//                    .creator( new PhoneNumber("+"+user.getCountryCode()+user.getPhoneNumber()), // to
//                            new PhoneNumber(env.getProperty("schematek.twilio.from")), // from
//                            "your schematek registry code: "+digitCode).create();
//            log.info("message send id:"+message.getSid()+">>>+"+countryCode+phoneNumber+" resend schematek verification code: "+digitCode);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found.");
        }

    }

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    public void sendResetPasswordUrl(String email, String userAgent, String host, String ip) {
        UUID signature = UUID.randomUUID();
        log.debug(">>>> userAgent:" + userAgent);
        log.debug(">>>> host:" + host);
        User user = getUserByEmail(email);
        if (user != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(env.getProperty("spring.mail.from"));
            message.setTo(email);
            message.setSubject("schematek reset password!");
            String text = String.format("Hi " + user.getFirstName() + ",\n" +
                    "You've recently asked to reset the password for this schematech account:\n" +
                    email + "\n" +
                    "To update your password, click the link below:\n" +
                    "http://" + host/*api.schematek.ca*/ + "/resetpass?signature=" + signature +
                    "&email=" + email +
                    "\n The schematech team.");
            message.setText(text);
            emailSender.send(message);

            //Save record...
            ResetPasswordRequest resetPassword = new ResetPasswordRequest();
            resetPassword.setHash(signature.toString());
//            Instant.now().getEpochSecond();
            resetPassword.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            resetPassword.setStatus(ResetPassStatus.PENDING);
            resetPassword.setType(ResetPassType.RESET_BY_EMAIL);
            resetPassword.setIp(ip);
            resetPassword.setUser(user);
            resetPassword.setUserAgent(userAgent);
            resetPasswordRepository.save(resetPassword);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user email not found!");
    }

    public void setNewPassword(NewPassPage newPassPage) {
        log.debug(newPassPage.toString());
        //check EnterPass && reEnterPass matched
        if (newPassPage.getNewPass() != null && !newPassPage.getNewPass().equals(newPassPage.getReNewPass())) {
            newPassPage.setError("Re entry password not matched.");
            return;
        }
        //get user if found...
        User user = getUserByEmail(newPassPage.getEmail());
        if (user == null) {
            newPassPage.setError("incorrect data, user not found.");
            return;
        }
        Optional<ResetPasswordRequest> oResetPasswordRequest = resetPasswordRepository.getHashRecord(newPassPage.getHash(), user);
        if (!oResetPasswordRequest.isPresent()) {
            newPassPage.setError("incorrect data, signature not found.");
            return;
        }
        //check not expire.
        ResetPasswordRequest r = oResetPasswordRequest.get();
        Timestamp oneHoureAgo = new Timestamp(System.currentTimeMillis());
        if ((r.getCreatedTime().getTime() + 1000 * 60 * 60) < oneHoureAgo.getTime()) {
            newPassPage.setError("reset password issue is expired. please try engine.");
            r.setStatus(ResetPassStatus.EXPIRED);
            resetPasswordRepository.save(r);
            return;
        }
        //save new password.
        user.setPassword(passwordEncoder.encode(newPassPage.getNewPass()));
        userRepository.save(user);
        //set status done.
        r.setStatus(ResetPassStatus.DONE);
        resetPasswordRepository.save(r);
        newPassPage.setMsg("password successfully changed.");
    }

    public void chengePassword(String userName, String oldPassword, String newPassword) {
        User user = getUserByPhoneNumber(userName);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found!");
        }
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "old password not matches!");
        }
    }


    public void verifyMail(String email) {
        log.debug("email:" + email);
        if (userRepository.countUserEmail(email) > 0)
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "this email used before !");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(env.getProperty("spring.mail.from"));
        message.setTo(email);
        message.setSubject("schematek verify EMail");
        String text = String.format("Hi " + email + ",\n" +
                "this email address is being set up for schematek account ...\n" +
//                email+"\n" +
//                "To update your password, click the link below:\n" +
//                "http://"+host/*api.schematek.ca*/+"/resetpass?signature="+ signature +
//                "&email="+email +
                "\n The schematek team.");
        message.setText(text);
        emailSender.send(message);

    }

    private CdnService cdnService;

    @Autowired
    public void setCdnService(CdnService cdnService) {
        this.cdnService = cdnService;
    }


    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private ImageService imageService;

    public MediaTO updateAvatar(String userName, MultipartFile avatar) throws Exception {
//        MediaTO mTo = new MediaTO();
        User user = getUserByPhoneNumber(userName);
        String bucketName = env.getProperty("schematek.s3.bucketname");
        //
        String randomKey = UUID.randomUUID() + "__" + avatar.getOriginalFilename();
        String key = "avatar/" + randomKey;
        String largeThumbkey = "avatar/LargeThumbnail/" + randomKey;
        String mediumThumbkey = "avatar/MediumThumbnail/" + randomKey;
        String smallThumbkey = "avatar/SmallThumbnail/" + randomKey;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(avatar.getSize());
//            awsS3.putObject( jobBucketName,key,fileMedia.getResource().getFile());
        cdnService.putObject(bucketName, key, avatar.getInputStream(), metadata);
        // video Media not supported until change inputStream in this code.
        cdnService.putObject(bucketName, largeThumbkey,
                imageService.resizeImage(avatar.getInputStream(), 800, 800), new ObjectMetadata());
        cdnService.putObject(bucketName, mediumThumbkey,
                imageService.resizeImage(avatar.getInputStream(), 306, 306), new ObjectMetadata());
        cdnService.putObject(bucketName, smallThumbkey,
                imageService.resizeImage(avatar.getInputStream(), 150, 150), new ObjectMetadata());

        Media media = new Media();
        media.setMediaUrl(key);
        //todo media.setMimeType ?...
        media.setLargeThumbUrl(largeThumbkey);
        media.setMediumThumbUrl(mediumThumbkey);
        media.setSmallThumbUrl(smallThumbkey);
        media.setMimeType(avatar.getContentType());
        media.setOrdering(0);
        media.setType(MediaType.USER_AVATAR);
        media.setUploadTime(new Timestamp(System.currentTimeMillis()));
        // video Media not supported until change inputStream in this code.
        BufferedImage image = ImageIO.read(avatar.getInputStream());
        media.setWidth(image.getWidth());
        media.setHeight(image.getHeight());
        mediaRepository.save(media);
        user.setAvatar(media);
        userRepository.save(user);
//        BeanUtils.copyProperties(media,mTo);
//        return mTo;
        return new MediaTO(media, bucketName, cdnService);
    }


    public void therapistRegister(Authentication authentication, Long id, String cv/*, MultipartFile documentFile1, MultipartFile documentFile2*/) {

        if (!authentication.getAuthorities().contains("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "admin role not found!");
        }

        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found.");
        User therapist = optionalUser.get();
        therapist.setIsTherapist(true);
        therapist.setCv(cv);
        //todo add Evidences
//        therapist.

    }

}
