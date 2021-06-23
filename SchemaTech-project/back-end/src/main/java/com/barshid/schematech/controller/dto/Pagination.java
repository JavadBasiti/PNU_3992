package com.barshid.schematech.controller.dto;

import lombok.Data;

@Data
public class Pagination {
    private Integer currentPage;
    private Integer	resultNo;
    private Integer paginationLimit;

}
