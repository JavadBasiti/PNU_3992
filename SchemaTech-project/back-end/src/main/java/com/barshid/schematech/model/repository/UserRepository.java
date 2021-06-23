package com.barshid.schematech.model.repository;

import com.barshid.schematech.model.bo.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends  CrudRepository<User, Long> {
    @Query("from User U WHERE U.email= ?1 ")
    List<User> findUserByEmail(String email);

    @Query("select  count(u.email)  from User u WHERE u.email= ?1 ")
    Integer countUserEmail(String email);

    @Query("from User U WHERE CONCAT(U.countryCode,U.phoneNumber)  = ?1 ")
    List<User> findUserByPhoneNumber(String phoneNumber);

    @Query("select count(*) from User u WHERE CONCAT(u.countryCode,u.phoneNumber)  = ?1 ")
    Integer countUserPhoneNumber(String phoneNumber);

    @Query("from User  u where  u.isTherapist=true ")
    List<User> getTherapList();

    @Query("from User  u where  u.therapist=?1 ")
    List<User> getPatients(User terapist);

}
