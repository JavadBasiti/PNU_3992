package com.barshid.schematech.model.bo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Question implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String QuestionnaireCode;
    private Integer no;
    private Double version;
    private String description;
    private String subject;

}
