package com.barshid.schematech.model.bo;

import com.barshid.schematech.model.GenderTypes;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
//@Table(name = "users" ,uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class User implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private Timestamp birthDate;
    private GenderTypes gender;
    private Integer countryCode;
    @Column(unique=true)
    private Long phoneNumber;
    @Column(unique=true)
    private String email;
    private String password;
    private String smspass;
    private Boolean enabled=true;
    private String blockedReason;
    private Timestamp blockedTime;
//    private String stripeCustomerID;
//    private Boolean verifiedPaymentMethod;
    @OneToOne
    private Media avatar;
    private Timestamp registerTime;
    private Timestamp lastLoginTime;
    private String lastLoginIP;
    private Float avgRate=0f;
    private Integer numRate=0;
    @ManyToMany
    @JoinTable(name="group_members" ,
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<UserGroup> groups = new ArrayList<UserGroup>();
//    @ElementCollection
//    private List<String> interestedKeywords;
//    private String notificationRegistry;
//    private String notificationSetting;
    private String address;

    //user have a therapist
    @ManyToOne
    User therapist;
    //or user is therapist
    @Column(nullable = false,columnDefinition = "boolean default false" )
    private Boolean isTherapist = false;
    private String cv;
    @OneToMany
    private List<Media> Evidences;
}