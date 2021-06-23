package com.barshid.schematech.service;

import com.barshid.schematech.controller.dto.AnswerEntery;
import com.barshid.schematech.controller.dto.AnswerTO;
import com.barshid.schematech.controller.dto.ResultTO;
import com.barshid.schematech.model.bo.Answer;
import com.barshid.schematech.model.bo.Question;
import com.barshid.schematech.model.bo.User;
import com.barshid.schematech.model.repository.AnswerRepository;
import com.barshid.schematech.model.repository.QuestionRepository;
import com.barshid.schematech.model.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class QuestionService {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public void saveAnswers(String username, AnswerEntery answersEntery) {
        log.debug("answersEntery"+answersEntery);
        List<User> userList = userRepository.findUserByPhoneNumber(username);
        if (userList == null || userList.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found !");
        User user = userList.get(0);
//        List<Question> questionList= questionRepository.getByquestionnaireCode(answersEntery.getQuestionnaireCode());
//        Question question=null;
//        if (questionList!=null && questionList.size()>0) question=questionList.get(0);
        for (AnswerTO an:answersEntery.getAnswers()) {
            Question question = questionRepository.getquestion(answersEntery.getQuestionnaireCode(), an.getQuestionNo());
            answerRepository.setDeletedFor(an.getQuestionNo(),question,user);
            answerRepository.save(new Answer (
                    question,
                    user,
                    an.getQuestionNo(),
                    answersEntery.getQuestionnaireCode(),
                    an.getAnswer(),
                    new Timestamp(System.currentTimeMillis())
            ));
        }
    }


    public List<AnswerTO> getAnswers(String username, Long userId, String qCode) {
        List<User> userList = userRepository.findUserByPhoneNumber(username);
        if (userList == null || userList.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found !");
        User user = userList.get(0);
        //todo terapist added.
        if (!user.getId().equals(userId))  throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "owner not accept !");
        List<AnswerTO> aTO=new ArrayList<>();
        for (Answer a:answerRepository.getAnswersBy(userId,qCode))  aTO.add(new AnswerTO(a));
        return aTO;

    }

    public List<ResultTO> getResults(String username, Long patientId, String qCode) {
        List<User> userList = userRepository.findUserByPhoneNumber(username);
        if (userList == null || userList.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found !");
        User user = userList.get(0);

        Optional<User> oPatient = userRepository.findById(patientId);
        if(!oPatient.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "patient not found !");
        User patient = oPatient.get();
        if (!userRepository.getPatients(user).contains(patient))  throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "owner not accept !");

        //                new ArrayList<>();
//        for (ResultTO rTO:answerRepository.getResults(patientId,qCode)  ) {
//            listRTO.add(new ResultTO(rTO.getClass().getField("subject").toGenericString(),rTO.getClass().getField("summery").get));
//            listRTO.add(rTO);
//            listRTO.add(rTO);
//        }
        return answerRepository.getResults(patientId,qCode);
    }

    public List<ResultTO> getUserResults(String username, String qCode) {
        List<User> userList = userRepository.findUserByPhoneNumber(username);
        if (userList == null || userList.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found !");
        User user = userList.get(0);
        return answerRepository.getResults(user.getId(),qCode);
    }
}
