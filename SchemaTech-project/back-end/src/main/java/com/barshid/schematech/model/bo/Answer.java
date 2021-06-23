package com.barshid.schematech.model.bo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
public class Answer implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @ManyToOne
    private Question question;
    @ManyToOne
    private User user;
    private Integer qNo;
    private String QuestionnaireCode;
    private Integer answer;
    private Timestamp date;
    private Timestamp deletedTime;

    public Answer() {
    }

    public Answer(Question question, User user, Integer qNo, String questionnaireCode, Integer answer, Timestamp date) {
        this.question = question;
        this.user = user;
        this.qNo = qNo;
        QuestionnaireCode = questionnaireCode;
        this.answer = answer;
        this.date = date;
    }
}
