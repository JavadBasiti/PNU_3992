package com.barshid.schematech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NativeSqlService {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void executeSQL(String sql){
        jdbcTemplate.execute( sql);

    }
    public List nativeQuery(String sql){
        return jdbcTemplate.queryForList(sql);

    }
}
