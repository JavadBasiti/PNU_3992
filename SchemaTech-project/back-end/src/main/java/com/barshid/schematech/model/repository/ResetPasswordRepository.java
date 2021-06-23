package com.barshid.schematech.model.repository;

import com.barshid.schematech.model.bo.ResetPasswordRequest;
import com.barshid.schematech.model.bo.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends CrudRepository<ResetPasswordRequest,Long> {

    @Query("from ResetPasswordRequest r WHERE r.hash= ?1 and r.user= ?2 and r.status = 1 ")
    public Optional<ResetPasswordRequest> getHashRecord(String hash, User user);
}
