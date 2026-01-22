package org.esrakonya.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);

        String port = context.getEnvironment().getProperty("local.server.port");
        String dbUrl = context.getEnvironment().getProperty("spring.datasource.url");

        logger.info("**********************************************************");
        logger.info("  SPRING BOOT ENGINE IS LIVE ON 8080 PORT {}", port);
        logger.info("  DATABASE URL: {}", dbUrl);
        logger.info("**********************************************************");
    }
}
