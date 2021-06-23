package com.barshid.schematech.model.repository;

import com.barshid.schematech.controller.dto.ResultTO;
import com.barshid.schematech.model.bo.Answer;
import com.barshid.schematech.model.bo.Question;
import com.barshid.schematech.model.bo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends CrudRepository<Answer,Long> {

    @Query("select MAX (a) from Answer a where a.user.id=?1 and a.QuestionnaireCode=?2 group by a.qNo ")
    List<Answer> getAnswersBy(Long userId,String qCode);

    @Query("from Answer a where a.deletedTime is null  and a.user.id=?1 and a.QuestionnaireCode=?2 group by a.qNo ")
    List<Answer> getActiveAnswersBy(Long userId,String qCode);

    @Query("select new com.barshid.schematech.controller.dto.ResultTO( a.question.subject ,SUM (a.answer) ) from Answer a where a.deletedTime is null and a.user.id=?1 and a.QuestionnaireCode=?2 group by a.question.subject ")
    List<ResultTO> getResults(Long patientId, String qCode);

    @Modifying( clearAutomatically = true)
    @Query("UPDATE  Answer as a SET a.deletedTime=CURRENT_TIMESTAMP  WHERE a.qNo=:questionNo and a.question= :question and a.user=:user ")
    void setDeletedFor(Integer questionNo, Question question, User user);
}
