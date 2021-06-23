package com.barshid.schematech.web.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnswerTO implements Serializable {
    private Integer questionNo;
    private Integer answer;

    //for AnswerEntery
    public AnswerTO() {
    }

}
