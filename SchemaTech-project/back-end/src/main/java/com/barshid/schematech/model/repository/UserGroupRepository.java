package com.barshid.schematech.model.repository;

import com.barshid.schematech.model.bo.UserGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends CrudRepository<UserGroup,Long> {
    @Query("from UserGroup ug WHERE ug.groupName = ?1 ")
    List<UserGroup> findUserGroupByName(String gName);

}
