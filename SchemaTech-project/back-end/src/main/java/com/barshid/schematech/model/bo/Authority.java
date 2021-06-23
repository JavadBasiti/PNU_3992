package com.barshid.schematech.model.bo;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "group_authorities")
public class Authority implements Serializable {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;

    @ManyToOne
    private UserGroup group;
    @Id @NotNull
    private String authority;

    public Authority() {
    }

    public Authority(String a, UserGroup g) {
        authority = a;
        group = g;
    }
}
