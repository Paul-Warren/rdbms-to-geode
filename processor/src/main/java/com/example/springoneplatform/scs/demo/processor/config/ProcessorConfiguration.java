package com.example.springoneplatform.scs.demo.processor.config;

import com.example.springoneplatform.scs.demo.model.etl.CustomerOrderPayload;
import com.example.springoneplatform.scs.demo.model.etl.CustomerPayload;
import com.example.springoneplatform.scs.demo.model.etl.ItemPayload;
import com.example.springoneplatform.scs.demo.processor.extractor.CustomerResultSetExtractor;
import com.example.springoneplatform.scs.demo.processor.extractor.ItemResultSetExtractor;
import com.example.springoneplatform.scs.demo.processor.handler.PayloadWrapperDao;
import com.example.springoneplatform.scs.demo.processor.extractor.CustomerOrderResultSetExtractor;
import com.example.springoneplatform.scs.demo.processor.handler.JdbcEventMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

import javax.sql.DataSource;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableBinding(Processor.class)
public class ProcessorConfiguration {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private SubscribableChannel input;

    @Autowired
    private MessageChannel output;

    @Autowired
    private JdbcEventMessageHandler jdbcEventMessageHandler;

    @Value("${customerSql}")
    private String customerSql;

    @Value("${itemSql}")
    private String itemSql;

    @Value("${customerOrderSql}")
    private String customerOrderSql;

    @Bean
    JdbcEventMessageHandler jdbcEventMessageHandler() {
        return new JdbcEventMessageHandler(context);
    }

    @Bean
    IntegrationFlow processorFlow() {
        return IntegrationFlows
                .from(input)
                .handle(jdbcEventMessageHandler)
                .channel(output)
                .get();
    }

    @Bean
    PayloadWrapperDao<CustomerPayload> customerDao(DataSource dataSource) {
        return new PayloadWrapperDao<>(dataSource, new CustomerResultSetExtractor(), customerSql);
    }

    @Bean
    PayloadWrapperDao<ItemPayload> itemDao(DataSource dataSource) {
        return new PayloadWrapperDao<>(dataSource, new ItemResultSetExtractor(), itemSql);
    }

    @Bean
    PayloadWrapperDao<CustomerOrderPayload> customerOrderDao(DataSource dataSource) {
        return new PayloadWrapperDao<>(dataSource, new CustomerOrderResultSetExtractor(), customerOrderSql);
    }
}
