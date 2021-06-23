package com.barshid.schematech.web.dto;

import lombok.Data;

@Data
public class ResultEntery implements Comparable<ResultEntery> {
    private String subject;
    private Long Summery;

    @Override
    public int compareTo(ResultEntery o) {
        return this.subject.compareTo(o.getSubject());
    }

}
