package com.barshid.schematech.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class UserTO implements Serializable,  Comparable<UserTO>  {
    private Long id;
    private String firstName;
    private String lastName;
    private Timestamp birthDate;
    private GenderTypes gender;
    private String email;
    private Integer countryCode;
    private Long phoneNumber;
    private MediaTO avatar;
    private String sickness;//todo New Feild.......
//    private Boolean varifiedPaymentMethod;
    private Boolean isTherapist;
    private String address;
    private PublicUser therapist;

    public Long getAge(){
        if (birthDate!=null) {
            return ( (System.currentTimeMillis() -birthDate.getTime()) / 31536000000L);//1000 * 60 * 60 * 24 * 365
        } else return 0l;

    }
    public void setAge(Long age){
    }

    @Override
    public int compareTo(UserTO o) {
        return this.id.compareTo(o.getId());
    }
}
