package com.barshid.schematech.controller;

import com.barshid.schematech.controller.dto.UserTO;
import com.barshid.schematech.service.TherapService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping({"therapist"})
public class TherapistControl {
    @Autowired
    private TherapService therapService;

    @ResponseBody
    @GetMapping({"/list"})
    public ResponseEntity<String> getTherapList() throws JsonProcessingException {
        return therapService.getTherapList();
    }

    @ResponseBody
    @GetMapping({"/setTherapist/{id}"})
    public void setTherapist(Authentication authentication,@PathVariable Long id)  {
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found with this token.");

        therapService.setTherapist(authentication.getName(),id);
    }

    @ResponseBody
    @GetMapping({"/patients"})
    public List<UserTO> setTherapist(Authentication authentication)  {
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found with this token.");

        return therapService.getPatients(authentication.getName());
    }
}
