package com.barshid.schematech.controller;

import com.barshid.schematech.controller.dto.*;
import com.barshid.schematech.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping({"user"})
public class UserControl {
    public UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
//    private UserGroupService userGroupService;

//    @Autowired
//    public void setUserGroupService(UserGroupService userGroupService) {
//        this.userGroupService = userGroupService;
//    }

    public UserControl() {
    }


    @ResponseBody
    @PostMapping(path = {""}, consumes = "application/json", produces = "application/json")
    public String register(@RequestHeader(name = "clientId", required = true) String clientIdEntry,
                           @RequestHeader(name = "clientSecret", required = true) String clientSecretEntry
            ,@RequestBody UserModifyTO userModifyTO) {
        ClientDetails c=clientDetailsService.loadClientByClientId(clientIdEntry);
        if(c!=null && clientSecretEntry!=null && passwordEncoder.matches(clientSecretEntry, c.getClientSecret()))
            return userService.register(userModifyTO);
        else return "client secret not matched!";
    }

    @ResponseBody
//    @GetMapping({"/userInfo"})
    @GetMapping({""})
    public UserTO userInfo(Authentication authentication) {//@RequestHeader String Authorization
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found with this token.");
        return this.userService.findUserByToken(authentication.getName());
    }


    @PutMapping({""})
    public void saveuserInfo(Authentication authentication,@RequestBody UserModifyTO userModifyTO) {//@RequestHeader String Authorization
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found with this token.");
        this.userService.updateUserByToken(authentication.getName(),userModifyTO);
    }

    @ResponseBody
    @GetMapping({"/{id}/profile"})
    public PublicUser getUser(@PathVariable String id, Authentication authentication) throws ResponseStatusException {//, HttpServletResponse res) {

            PublicUser uto= this.userService.findUserById(Long.valueOf(id));
//            if (!authentication.getName().equals(uto.getCountryCode().toString()+uto.getPhoneNumber().toString()))
//                   throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "id not for this user.");
            return uto;
    }

    @PostMapping({"/phoneLogin"})
    public void phoneLogin(Long phoneNumber ,Long countryCode,
                           @RequestHeader(name = "clientId", required = true) String clientIdEntry,
                           @RequestHeader(name = "clientSecret", required = true) String clientSecretEntry){
        ClientDetails c=clientDetailsService.loadClientByClientId(clientIdEntry);
        if(c!=null && clientSecretEntry!=null && passwordEncoder.matches(clientSecretEntry, c.getClientSecret()))
            userService.sendVerificationCode(phoneNumber,countryCode);
        else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"client secret not matched!");


    }

    @PostMapping({"/forgetPassword"})
    public void forgetPassword(String email ,
                           @RequestHeader(value = "User-Agent") String userAgent,
                           @RequestHeader(value = "Host") String host,
                           @RequestHeader(name = "clientId", required = true) String clientIdEntry,
                           @RequestHeader(name = "clientSecret", required = true) String clientSecretEntry,
                           HttpServletRequest request){
        log.debug(">>>>>request.getRemoteAddr():"+request.getRemoteAddr());
        log.debug(">>>>>request.getLocalAddr():"+request.getLocalAddr());
        log.debug(">>>>>request.getUserPrincipal():"+request.getUserPrincipal());
        log.debug(">>>>>request.getRemoteUser():"+request.getRemoteUser());
        log.debug(">>>>>request.getRequestURI():"+request.getRequestURI());
        log.debug(">>>>>request.getPathInfo():"+request.getPathInfo());
        log.debug(">>>>>request.getRequestURL():"+request.getRequestURL());
        log.debug(">>>>>request.getRemoteHost():"+request.getRemoteHost());
        ClientDetails c=clientDetailsService.loadClientByClientId(clientIdEntry);
        if(c!=null && clientSecretEntry!=null && passwordEncoder.matches(clientSecretEntry, c.getClientSecret()))
            userService.sendResetPasswordUrl(email,userAgent,host,request.getRemoteAddr());
        else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"client secret not matched!");
    }

    @ResponseBody
    @PostMapping({"/changePassword/"})
    public void chengePassword(Authentication authentication,
                               @RequestBody Map<String,String> values){
//                               oldPassword,
//                               @RequestBody String newPassword){
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found with this token.");
        userService.chengePassword(authentication.getName(),values.get("oldPassword"),values.get("newPassword"));

    }



    @ResponseBody
    @PostMapping({"/verifyMail"})
    public void verifyMail(@RequestBody Map<String,String> keyEmail) {//@RequestHeader String Authorization
//        if (authentication instanceof AnonymousAuthenticationToken)
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found with this token.");
        this.userService.verifyMail(keyEmail.get("email"));
    }




    @ResponseBody
    @PostMapping({"/updateAvatar"})
    public MediaTO updateAvatar(Authentication authentication,@RequestParam("avatar") MultipartFile avatar) throws Exception {//@RequestHeader String Authorization
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found with this token.");
        return this.userService.updateAvatar(authentication.getName(),avatar);
    }



    @Autowired
    private PasswordEncoder passwordEncoder;
//    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }

    private org.springframework.security.oauth2.provider.ClientDetailsService clientDetailsService;
    @Autowired
    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    @PostMapping({"/{id}/therapistRegister"})
    public void becomeTherapist(Authentication authentication,@PathVariable Long id,
                             @RequestParam("CV") String cv/*,
                             @RequestParam("documentFile1") MultipartFile documentFile1,
                             @RequestParam("documentFile2") MultipartFile documentFile2*/) {
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found with this token.");

        userService.therapistRegister(authentication,id,cv/*,documentFile1,documentFile2*/);
    }


}
