package com.barshid.schematech.web;

import lombok.Data;
import org.springframework.web.context.annotation.SessionScope;

import javax.inject.Named;
import java.sql.Timestamp;

@Data
@Named
@SessionScope
public class Token {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private Timestamp expires_in;
    private String scope;
}
