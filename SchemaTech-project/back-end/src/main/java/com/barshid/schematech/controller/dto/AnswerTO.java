package com.barshid.schematech.controller.dto;

import com.barshid.schematech.model.bo.Answer;
import lombok.Data;

import java.io.Serializable;

@Data
public class AnswerTO implements Serializable {
    private Integer questionNo;
    private Integer answer;

    //for AnswerEntery
    public AnswerTO() {
    }

    public AnswerTO(Answer a) {
        questionNo = a.getQNo();
        answer=a.getAnswer();
    }
}
