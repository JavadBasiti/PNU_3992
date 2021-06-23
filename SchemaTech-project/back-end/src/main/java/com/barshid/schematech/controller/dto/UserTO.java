package com.barshid.schematech.controller.dto;

import com.barshid.schematech.model.GenderTypes;
import com.barshid.schematech.model.bo.User;
import com.barshid.schematech.service.CdnService;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Timestamp birthDate;
    private GenderTypes gender;
    private String email;
    private Integer countryCode;
    private Long phoneNumber;
    private MediaTO avatar;
//    private Boolean varifiedPaymentMethod;
    private Boolean isTherapist;
    private String address;
    private PublicUser therapist;


    public UserTO(User user, String bucketName, CdnService cdnService) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        countryCode = user.getCountryCode();
        phoneNumber = user.getPhoneNumber();
        if(user.getAvatar()!=null)
            avatar = new MediaTO( user.getAvatar(),bucketName, cdnService);
//        varifiedPaymentMethod = user.getVerifiedPaymentMethod();
        isTherapist = user.getIsTherapist();
        birthDate = user.getBirthDate();
        gender = user.getGender();
        address= user.getAddress();
        if (user.getTherapist()!=null)
            therapist= new PublicUser(user.getTherapist(), bucketName, cdnService);
    }
}
