package com.barshid.schematech.model.bo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
public class ResetPasswordRequest implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    private ResetPassStatus status;
    private String hash;
    private Timestamp createdTime;
    private ResetPassType type;
    private String userAgent;
    private String ip;

}
