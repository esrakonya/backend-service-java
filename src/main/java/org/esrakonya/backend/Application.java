package org.esrakonya.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        String env = System.getenv("APP_ENV");

        if (env == null) {
            env = "LOCAL";
        }

        //Testing different logging levels
        logger.debug("DEBUG LEVEL: This message is for developers only.");
        logger.info("INFO LEVEL: Application started successfully in {} environment.", env);
        logger.warn("WARN LEVEL: This is a warning message. Potential issue detected.");
        logger.error("ERROR LEVEL: Critical error occurred! Action required.");
    }
}
