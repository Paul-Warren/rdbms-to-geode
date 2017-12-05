package com.example.springoneplatform.scs.demo.source;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.app.jdbc.source.JdbcSourceConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({JdbcSourceConfiguration.class})
public class SourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SourceApplication.class, args);
    }
}
