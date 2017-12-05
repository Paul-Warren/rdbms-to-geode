package com.example.springoneplatform.scs.demo.sink.config;

import com.example.springoneplatform.scs.demo.model.etl.CustomerOrderPayload;
import com.example.springoneplatform.scs.demo.model.etl.CustomerPayload;
import com.example.springoneplatform.scs.demo.model.etl.ItemPayload;
import com.example.springoneplatform.scs.demo.model.pdx.Customer;
import com.example.springoneplatform.scs.demo.model.pdx.CustomerOrder;
import com.example.springoneplatform.scs.demo.model.pdx.Item;
import com.example.springoneplatform.scs.demo.sink.extractor.CustomerExtractor;
import com.example.springoneplatform.scs.demo.sink.extractor.CustomerOrderExtractor;
import com.example.springoneplatform.scs.demo.sink.extractor.ItemExtractor;
import com.example.springoneplatform.scs.demo.sink.extractor.PayloadWrapperExtractor;
import com.example.springoneplatform.scs.demo.sink.messaging.GeodeMessageAggregator;
import com.example.springoneplatform.scs.demo.sink.messaging.GeodeMessageHandler;
import org.apache.geode.cache.Cache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.PdxSerializer;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.dsl.AggregatorSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.support.Consumer;
import org.springframework.messaging.SubscribableChannel;

import javax.annotation.PreDestroy;

@Configuration
@EnableBinding(Sink.class)
public class SinkConfiguration {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private SubscribableChannel input;

    @Value("${aggregator.groupCount}")
    private int aggregatorGroupCount;

    @Value("${aggregator.batchSize}")
    private int aggregatorBatchSize;

    @Value("${aggregator.batchTimeout}")
    private int aggregatorBatchTimeout;

    @Value("${geode.locator}")
    private String geodeLocator;

    @Value("${geode.port}")
    private int geodeLocatorPort;

    @Autowired
    private GeodeMessageAggregator geodeAggregator;

    @Autowired
    private Consumer<AggregatorSpec> geodeAggregateConsumer;

    @Autowired
    private ClientCache clientCache;

    @Bean
    GeodeMessageAggregator geodeAggregator() {
        return new GeodeMessageAggregator(context, aggregatorGroupCount, aggregatorBatchSize);
    }

    @Bean
    Consumer<AggregatorSpec> geodeAggregateConsumer() {
        return aggregatorSpec -> {
            aggregatorSpec.processor(geodeAggregator, null);
            aggregatorSpec.groupTimeout(aggregatorBatchTimeout);
            aggregatorSpec.sendPartialResultOnExpiry(true);
            aggregatorSpec.expireGroupsUponCompletion(true);
            aggregatorSpec.expireGroupsUponTimeout(true);
            // TODO: persisted message store
        };
    }

    @Bean
    IntegrationFlow sinkFlow(ClientCache clientCache) {
        return IntegrationFlows.from(input)
                .aggregate(geodeAggregateConsumer)
                .handle(new GeodeMessageHandler(clientCache))
                .get();
    }

    @Bean
    PayloadWrapperExtractor<CustomerPayload, Customer> customerExtractor() {
        return new CustomerExtractor();
    }

    @Bean
    PayloadWrapperExtractor<CustomerOrderPayload, CustomerOrder> customerOrderExtractor() {
        return new CustomerOrderExtractor();
    }

    @Bean
    PayloadWrapperExtractor<ItemPayload, Item> itemExtractor() {
        return new ItemExtractor();
    }

    @Bean
    @ConditionalOnMissingBean({ Cache.class, ClientCache.class })
    @ConditionalOnClass({ ClientCache.class })
    ClientCache clientCache(PdxSerializer pdxSerializer) {
        // TODO: access the locator from binding service
        return new ClientCacheFactory()
                .setPdxSerializer(pdxSerializer)
                .addPoolLocator(geodeLocator, geodeLocatorPort)
                .create();
    }

    @Bean
    ClientRegionFactory clientRegionFactory(ClientCache clientCache) {
        return clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY);
    }

    @Bean
    PdxSerializer pdxSerializer() {
        return new ReflectionBasedAutoSerializer("io.pivotal.scs.demo.model.geode.pdx.*");
    }

    @Bean
    Region customerRegion(ClientRegionFactory clientRegionFactory) {
        return clientRegionFactory.create("customer");
    }

    @Bean
    Region customerOrderRegion(ClientRegionFactory clientRegionFactory) {
        return clientRegionFactory.create("customerOrder");
    }

    @Bean
    Region itemRegion(ClientRegionFactory clientRegionFactory) {
        return clientRegionFactory.create("item");
    }

    @PreDestroy
    public void cleanUp() throws Exception {
        clientCache.close();
    }

    @Profile("default")
    protected static class DefaultConfiguration {
        @Bean
        @ConditionalOnMissingBean({ Cache.class, ClientCache.class })
        @ConditionalOnClass({ ClientCache.class })
        ClientCache clientCache(PdxSerializer pdxSerializer) {
            return new ClientCacheFactory()
                    .setPdxSerializer(pdxSerializer)
                    .create();
        }

        @Bean
        ClientRegionFactory clientRegionFactory(ClientCache clientCache) {
            return clientCache.createClientRegionFactory(ClientRegionShortcut.LOCAL);
        }
    }
}
