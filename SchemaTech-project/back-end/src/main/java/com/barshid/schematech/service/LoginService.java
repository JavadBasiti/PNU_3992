package com.barshid.schematech.service;

import com.barshid.schematech.controller.dto.LoginParams;
import com.barshid.schematech.model.bo.User;
import com.barshid.schematech.model.repository.UserRepository;
import com.barshid.schematech.model.security.UserSecurityConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class LoginService {
    UserRepository userRepository;
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, String> tokenParameter(LoginParams loginParams){
        Map<String, String> parameters = new HashMap<>();
        try {
            if(loginParams.getEmail()!=null && loginParams.getEmail().contains("@")){
                //email Login...
                log.debug("log param email:"+loginParams.getEmail());
                final List<User> uList = userRepository.findUserByEmail(loginParams.getEmail());
                if (uList.isEmpty()) throw new Exception("user not find by this email.");
                parameters.put("username",uList.get(0).getCountryCode().toString()+uList.get(0).getPhoneNumber().toString());
                parameters.put("password",loginParams.getPassword());
                parameters.put("grant_type","password");
//                if(u.toString().contains("smspass as password")) {
                UserSecurityConfiguration.updateUsernameQuery("select CONCAT(country_code,phone_number) as username,password,enabled " +
                        "from user where CONCAT(country_code,phone_number) = ?");
//                }

            }else if(loginParams.getPhoneNumber()!=null){
                //phone Login...
//                if(loginParams.getPhoneNumber()==null || loginParams.getPhoneNumber()==0)throw new Exception("invalid phone number.");
                if("".equals(loginParams.getPhoneNumber()))     throw new Exception("invalid phone number.");
                if(loginParams.getCountryCode()==null || loginParams.getCountryCode()==0) throw new Exception("invalid country code .");
                parameters.put("username",loginParams.getCountryCode().toString()+loginParams.getPhoneNumber().toString());
                //sms code...
                parameters.put("password",loginParams.getVerificationCode());
//                final String verificationCode = loginParams.getVerificationCode();
//                final List<User> uList = userRepository.findUserByPhoneNumber(loginParams.getCountryCode().toString()+loginParams.getPhoneNumber().toString());
//                if (uList.isEmpty())throw new Exception("user not found.");
//                if (uList.get(0).getSmspass().equals(loginParams.getVerificationCode())) {
//                    PasswordEncoder pe = new BCryptPasswordEncoder();
//                    //todo endodind password??????????? not mach
//                    parameters.put("password", uList.get(0).getPassword());
//                }else {
//                    throw new Exception("verification code is wrong.");
//                }
                parameters.put("grant_type","password");
//                if(u.toString().contains("password as password")) {
                UserSecurityConfiguration.updateUsernameQuery("select CONCAT(country_code,phone_number) as username,smspass as password,enabled " +
                            "from user where CONCAT(country_code,phone_number) = ?");
//                }

            }else if(loginParams.getRefreshToken()!=null){ //refresh_token
                log.debug("refresh_token:"+loginParams.getRefreshToken());
                parameters.put("refresh_token",loginParams.getRefreshToken());
                parameters.put("grant_type","refresh_token");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return parameters;
    }
}
