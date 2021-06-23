package com.barshid.schematech.web.pagebean;

import lombok.Data;

@Data
public class NewPassPage {
    private String email;
    private String hash;
    private String newPass;
    private String reNewPass;
    private String msg;
    private String error;
}
