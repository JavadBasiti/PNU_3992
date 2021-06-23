package com.barshid.schematech.controller.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Data
public class ApiError {
    private Timestamp timestamp;
    private Integer status;
    private HttpStatus error;
//    private List<String> message;
    private String message;
    private String path;


    public ApiError(HttpStatus error, String message,String path) {
        super();
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.status = error.value();
        this.error = error;
        this.message = message;//Arrays.asList(message);
        this.path = path;

    }
}