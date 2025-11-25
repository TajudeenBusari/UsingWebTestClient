package com.tjetchy.CheckingOutRestTestClient.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;

///**
// * This class helps us print relevant info about the database.
// */
////@Component
////public class DatabaseChecker implements CommandLineRunner {
////    private static final Logger logger = LoggerFactory.getLogger(DatabaseChecker.class);
////    @Autowired
////    private ReactiveMongoTemplate reactiveMongoTemplate;
////    @Autowired
////    private Environment environment;
////
////    @Override
////    public void run(String... args) throws Exception {
////        logger.info("=== REACTIVE MONGODB CONFIGURATION CHECK ===");
////        //get actual database from the configuration
////        String databaseName = environment.getProperty("spring.data.mongodb.name", "todosdb");
////        String mongoUri = environment.getProperty("spring.data.mongodb.uri", "not-set");
////
//////        logger.info("Mongo URI: {}", environment.getProperty("spring.data.mongodb.uri"));
////        logger.info("Mongo DB NAME: {}", databaseName);
////        logger.info("Mongo DB URL: {}", mongoUri.replace("*****", "apppass"));
////
////        // Use the specific database instead of default
////        reactiveMongoTemplate.getCollectionNames()
////                .doOnNext(collection -> logger.info("Collection Name: {}", collection))
////                .doOnComplete(() -> logger.info("=== REACTIVE CONNECTION SUCCESSFUL ==="))
////                .doOnError(error -> logger.info("Error: {}", error.getMessage()))
////                .subscribe();
////    }
//
//}
