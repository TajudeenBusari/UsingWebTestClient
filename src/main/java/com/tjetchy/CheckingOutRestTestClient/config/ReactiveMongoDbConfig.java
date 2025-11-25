package com.tjetchy.CheckingOutRestTestClient.config;


import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

/*
 * This auto-configuration class for reactive MongoDB was buggy (Still need to confirm this)
 * with 4.0.0-RC1. This custom config class bypass the buggy auto-config and creates
 * the MongoDB bean manually. Instead of relying on the application.yml,
 * we are manually creating the connection with the exact parameters:
 * MongoClients.create(...).
 * The primary annotation means when there are multiple MongoDB Clients, use
 * this one as main one. This ensures custom configuration takes precedence over
 * any autoconfigured ones
 * Doc of how to use MongoDb in Spring boot:
 * <a href="https://docs.spring.io/spring-data/mongodb/reference/mongodb/configuration.html">...</a>
 *
 */
@Configuration
@Profile("!test") //makes sure this config is not loaded during test, so we can use the test config.
public class ReactiveMongoDbConfig {
    private static final Logger logger = LoggerFactory.getLogger(ReactiveMongoDbConfig.class);

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Bean
    @Primary
    public MongoClient reactiveMongoClient() {
        logger.info("=== FORCING MONGODB CONNECTION WITH URI: {} ====",
                mongoUri.replace("apppass", "******"));
        return MongoClients.create(mongoUri);
    }

    /**
     *This template actually creates the todosdb, without it, requests try to go to the default database
     * test,which will be unsuccessful
     *
     */
    @Bean
    @Primary
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        ReactiveMongoTemplate template = new ReactiveMongoTemplate(reactiveMongoClient(), "todosdb");
        logger.info("=== REACTIVE MONGO TEMPLATE TO: {} ===", template);
        return template;
    }


}
