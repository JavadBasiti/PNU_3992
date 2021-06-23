package com.barshid.schematech.model.bo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
public class UserRate  implements Serializable {
    @Id
    private Timestamp createdTime;
    @OneToOne
    private User from;
    @OneToOne
    private User to;
    private Integer rate;
    private String userIP;

    public UserRate() {
    }

    public UserRate(Timestamp createdTime, User from, User to, Integer rate, String userIP) {
        this.createdTime = createdTime;
        this.from = from;
        this.to = to;
        this.rate = rate;
        this.userIP = userIP;
    }
}
