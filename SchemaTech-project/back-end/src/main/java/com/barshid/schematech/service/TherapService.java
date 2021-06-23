package com.barshid.schematech.service;

import com.barshid.schematech.controller.dto.TherapistTO;
import com.barshid.schematech.controller.dto.UserTO;
import com.barshid.schematech.model.bo.User;
import com.barshid.schematech.model.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class TherapService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ObjectMapper jsonMapper;

    public ResponseEntity<String> getTherapList() throws JsonProcessingException {
        List<TherapistTO> tTos =new ArrayList<>();
        for (User aUser:userRepository.getTherapList()) {
            tTos.add(new TherapistTO(aUser));
        }
        if (tTos.size()==0) return new ResponseEntity<String>("{ \"therapistes\": []}",HttpStatus.NO_CONTENT);
        return new ResponseEntity<>("{ \"therapistes\": "+jsonMapper.writeValueAsString(tTos)+"}", HttpStatus.OK) ;
    }

    public void setTherapist(String username, Long id) {

    }

    @Autowired
    private Environment env;
    private CdnService cdnService;
    @Autowired
    public void setCdnService(CdnService cdnService) {
        this.cdnService = cdnService;
    }

    public List<UserTO> getPatients(String username) {
        List<User> userList = userRepository.findUserByPhoneNumber(username);
        if (userList == null || userList.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found !");
        User user = userList.get(0);
        List<UserTO> listUto= new ArrayList<>();
        if (user.getIsTherapist()){
            for (User u:
            userRepository.getPatients(user)) {
                listUto.add(new UserTO(u,env.getProperty("schematek.s3.bucketname"), cdnService));
            }
        }
        return listUto;
    }
}
