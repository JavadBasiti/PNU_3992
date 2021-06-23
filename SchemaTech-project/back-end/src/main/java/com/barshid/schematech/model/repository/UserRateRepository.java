package com.barshid.schematech.model.repository;

import com.barshid.schematech.model.bo.UserRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface UserRateRepository extends CrudRepository<UserRate, Timestamp> {
}
