package com.barshid.schematech.controller.dto;

import com.barshid.schematech.model.bo.User;
import com.barshid.schematech.service.CdnService;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class PublicUser {
    private Long id;
    private String name;
    private MediaTO avatar;
    private Boolean isTherapist ;
    private Timestamp memberFrom;
    private Integer  jobsPostedNo;
    private Integer jobsDoneNo;
    private Integer reviewsNo;


    public PublicUser(User user, String bucketName, CdnService cdnService) {
        id = user.getId();
        name = user.getFirstName()+" "+ user.getLastName();
        if (user.getAvatar()!=null)
            avatar = new MediaTO(user.getAvatar(),bucketName, cdnService );
        isTherapist = user.getIsTherapist();
        memberFrom=user.getRegisterTime();
        jobsPostedNo = user.getNumRate();
        if(user.getAvgRate()!=null) jobsDoneNo = user.getAvgRate().intValue();
        reviewsNo = user.getNumRate();
    }
}
