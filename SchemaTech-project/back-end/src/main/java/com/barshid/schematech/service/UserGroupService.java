package com.barshid.schematech.service;

import com.barshid.schematech.model.bo.UserGroup;
import com.barshid.schematech.model.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserGroupService {
    private UserGroupRepository userGroupRepository;
    @Autowired
    public void setUserGroupRepository(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    public UserGroup getUserGroupByName(String gName){
        List<UserGroup> lug = userGroupRepository.findUserGroupByName(gName);
        if (!lug.isEmpty()){
            return lug.get(0);
        }
        return null;
    }
}
