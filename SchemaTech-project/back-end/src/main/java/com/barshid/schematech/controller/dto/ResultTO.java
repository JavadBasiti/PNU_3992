package com.barshid.schematech.controller.dto;

import lombok.Data;

@Data
public class ResultTO {
    private String subject;
    private Long summery;

    public ResultTO(String subject, Long summery) {
        this.subject = subject;
        this.summery = summery;
    }
}
