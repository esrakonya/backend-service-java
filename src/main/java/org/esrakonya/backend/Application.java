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

        logger.error(">>> DATASOURCE URL = {}",
                context.getEnvironment().getProperty("spring.datasource.url"));

        logger.error(">>> DATASOURCE USER = {}",
                context.getEnvironment().getProperty("spring.datasource.username"));

        logger.error(">>> SERVER PORT = {}",
                context.getEnvironment().getProperty("server.port"));
    }
}
