package com.barshid.schematech.controller;

import com.barshid.schematech.controller.dto.AnswerEntery;
import com.barshid.schematech.controller.dto.AnswerTO;
import com.barshid.schematech.controller.dto.ResultTO;
import com.barshid.schematech.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping({"questionnaire"})
public class QuestionnaireControl {
    @Autowired
    private QuestionService questionService;


    @PostMapping({"/answers"})
    public void saveAnswers(Authentication authentication,
                            @RequestBody AnswerEntery answersEntery){
        if (authentication ==null || authentication instanceof AnonymousAuthenticationToken)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found with this token.");
        questionService.saveAnswers(authentication.getName(),answersEntery);
    }

    @ResponseBody
    @GetMapping({"/{userId}/{qCode}"})
    public List<AnswerTO> getAnswers(Authentication authentication, @PathVariable Long userId, @PathVariable String qCode){
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found with this token.");
        return  questionService.getAnswers(authentication.getName(),userId,qCode);
    }
    @ResponseBody
    @GetMapping({"/{patientId}/results/{qCode}"})
    public List<ResultTO> getResults(Authentication authentication, @PathVariable Long patientId, @PathVariable String qCode) throws NoSuchFieldException {
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found with this token.");
        return  questionService.getResults(authentication.getName(),patientId,qCode);
    }
    @ResponseBody
    @GetMapping({"/results/{qCode}"})
    public List<ResultTO> getUserResults(Authentication authentication,  @PathVariable String qCode) throws NoSuchFieldException {
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found with this token.");
        return  questionService.getUserResults(authentication.getName(),qCode);
    }

}
