package com.barshid.schematech.model.repository;


import com.barshid.schematech.model.bo.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository  extends CrudRepository<Question,Long> {
    @Query("from Question q where q.QuestionnaireCode=?1 " )
    List<Question> getByquestionnaireCode(String code);

    @Query("from Question q where q.QuestionnaireCode=?1 and q.no=?2 " )
    Question getquestion(String code,Integer no);
}
