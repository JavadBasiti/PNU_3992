package com.barshid.schematech.web.dto;

import lombok.Data;

@Data
public class Pagination {
    private Integer currentPage;
    private Integer	resultNo;
    private Integer paginationLimit;

}
