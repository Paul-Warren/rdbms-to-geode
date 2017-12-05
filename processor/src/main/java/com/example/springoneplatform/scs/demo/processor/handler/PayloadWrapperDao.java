package com.example.springoneplatform.scs.demo.processor.handler;

import com.example.springoneplatform.scs.demo.model.etl.PayloadWrapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class PayloadWrapperDao<T> {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final ResultSetExtractor<PayloadWrapper<T>> messageBuilderExtractor;
    private final String sql;

    public PayloadWrapperDao(DataSource dataSource, ResultSetExtractor<PayloadWrapper<T>> resultSetExtractor, String sql) {
        this.jdbcOperations = new NamedParameterJdbcTemplate(dataSource);
        this.messageBuilderExtractor = resultSetExtractor;
        this.sql = sql;
    }

    public PayloadWrapper<T> getData(String srcKey) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("srcKey", srcKey);
        return this.jdbcOperations.query(sql, paramMap, messageBuilderExtractor);
    }

}
