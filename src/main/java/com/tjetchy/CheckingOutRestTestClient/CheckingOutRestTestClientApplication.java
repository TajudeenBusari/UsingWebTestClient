package com.tjetchy.CheckingOutRestTestClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication

public class CheckingOutRestTestClientApplication {
    private static final Logger logger = LoggerFactory.getLogger(CheckingOutRestTestClientApplication.class);
    @Value("${api.endpoint.baseurl:NOT_FOUND}")
    private String baseUrl;

	public static void main(String[] args) {
		SpringApplication.run(CheckingOutRestTestClientApplication.class, args);
	}
    @Bean
    public CommandLineRunner checkMongoConfig(Environment environment) {
        return args -> {
            logger.info("=== CHECKING MONGODB CONFIG AND PROPERTIES ===");
            String mongoUri = environment.getProperty("spring.data.mongodb.uri");
            logger.info("MONGODB URI from properties: {}", mongoUri);
            logger.info("*******api.endpoint.base-url*********: {}", baseUrl);
            logger.info("=== END OF MONGODB CONFIG CHECK =====");
        };
    }

}
