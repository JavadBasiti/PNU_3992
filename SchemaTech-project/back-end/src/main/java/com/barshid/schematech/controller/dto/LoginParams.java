package com.barshid.schematech.controller.dto;

import lombok.Data;

@Data
public class LoginParams {
    private String email;
    private String password;
    private Integer countryCode;
    private String phoneNumber;
    private String verificationCode;
    private String refreshToken;

    @Override
    public String toString() {
        return "LoginParams{" +
                "email='" + email + '\'' +
                ", Password='" + password + '\'' +
                ", countryCode=" + countryCode +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }
}
