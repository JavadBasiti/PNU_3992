package com.barshid.schematech.web.dto;

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

}
