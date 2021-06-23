package com.barshid.schematech.controller.dto;

import com.barshid.schematech.model.bo.User;
import lombok.Data;

@Data
public class TherapistTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String cv;

    public TherapistTO(User user) {
        id= user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        cv = user.getCv();
    }
}
