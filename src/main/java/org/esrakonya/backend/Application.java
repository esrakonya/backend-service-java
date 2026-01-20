package org.esrakonya.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        String environment = System.getenv("APP_ENV");

        if (environment == null) {
            environment = "local";
        }

        logger.info("Application started in environment: {}", environment);
    }
}
