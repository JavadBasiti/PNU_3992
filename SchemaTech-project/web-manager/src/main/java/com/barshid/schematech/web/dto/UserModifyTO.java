package com.barshid.schematech.web.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserModifyTO {
    private String firstName;
    private String lastName;
    private Timestamp birthDate;
    private GenderTypes gender;
    private String email;
    private Integer countryCode;
    private Long phoneNumber;
    private String password;
    private String address;
    private Long therapistId;

}
