package com.barshid.schematech.web.dto;

import lombok.Data;

@Data
public class LoginParams {
    private String email;
    private String password;
    private Integer countryCode;
    private String phoneNumber;
    private String verificationCode;
    private String refreshToken;

}
