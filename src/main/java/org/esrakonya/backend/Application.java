package org.esrakonya.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        // This is the core line that starts Spring Boot and Tomcat
        SpringApplication.run(Application.class, args);

        logger.info("**********************************************************");
        logger.info("  SPRING BOOT ENGINE IS LIVE ON PORT 8080");
        logger.info("**********************************************************");
    }
}
