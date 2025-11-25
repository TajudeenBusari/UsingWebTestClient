//package com.tjetchy.CheckingOutRestTestClient.config;
//
//
//import com.mongodb.reactivestreams.client.MongoClient;
//import com.mongodb.reactivestreams.client.MongoClients;
//import jakarta.annotation.PostConstruct;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
//
///**
// * This auto-configuration class for reactive MongoDB was buggy (Still need to confirm this)
// * with 4.0.0-RC1. This custom config class bypass the buggy auto-config and creates
// * the MongoDB bean manually. Instead of relying on the application.yml,
// * we are manually creating the connection with the exact parameters:
// * MongoClients.create(...).
// * The primary annotation means when there are multiple MongoDB Clients, use
// * this one as main one. This ensures custom configuration takes precedence over
// * any autoconfigured ones
// * Doc of how to use MongoDb in Spring boot:
// * <a href="https://docs.spring.io/spring-data/mongodb/reference/mongodb/configuration.html">...</a>
// *
// */
//@Configuration
//@EnableConfigurationProperties(MongoConfigProperties.class)
//public class ReactiveMongoConfig {
//
////    @Value("${MONGO_HOST:localhost}")
//////    @Value("${mongo.host}")
////    private String host;
////
//////    @Value("${mongo.user}")
////    @Value("${MONGO_USER:tjtechy}")
////    private String username;
////
////    //@Value("${mongo.pass}")
////    @Value("${MONGO_PASS:apppass}")
////    private String password;
//
//    private final MongoConfigProperties mongoConfigProperties;
//    public ReactiveMongoConfig(MongoConfigProperties mongoConfigProperties) {
//    this.mongoConfigProperties = mongoConfigProperties;
//    }
//
//    private  ReactiveMongoTemplate reactiveMongoTemplate;
//    private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveMongoConfig.class);
//
//    @PostConstruct
//    public void logConfiguration() {
//        LOGGER.info("=== REACTIVE MONGO CONFIG LOADED ===");
//        LOGGER.info("Host: {}", this.mongoConfigProperties.getHost());
//        LOGGER.info("Username: {}", this.mongoConfigProperties.getUser());
//        LOGGER.info("MONGO_PASS: {}", this.mongoConfigProperties.getPassword().replace(".", "*"));
//    }
//    @Bean
//    @Primary
//    public MongoClient reactiveMongoClient() {
//        String connectionString = "mongodb://%s:%s@%s:27017/todosdb?authSource=todosdb"
//                .formatted(mongoConfigProperties.getUser(), mongoConfigProperties.getPassword(), mongoConfigProperties.getHost());
//        LOGGER.info("=== FORCING MONGO CONNECTION TO: {} ===", connectionString);
//        return MongoClients.create(connectionString);
//    }
//
//    @Bean
//    @Primary
//    public ReactiveMongoTemplate reactiveMongoTemplate() {
//        ReactiveMongoTemplate template = new ReactiveMongoTemplate(reactiveMongoClient(), "todosdb");
//        LOGGER.info("=== REACTIVE MONGO TEMPLATE TO: {} ===", template);
//        return template;
//    }
//}
