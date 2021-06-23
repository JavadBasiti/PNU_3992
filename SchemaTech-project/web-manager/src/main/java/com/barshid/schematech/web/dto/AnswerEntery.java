package com.barshid.schematech.web.dto;

//import com.barshid.schematech.model.bo.Answer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AnswerEntery implements Serializable {
    private String questionnaireCode;
    private List<AnswerTO> answers;

}
