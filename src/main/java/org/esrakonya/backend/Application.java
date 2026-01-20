package org.esrakonya.backend;

public class Application {

    public static void main(String[] args) {
        String environment = System.getenv("APP_ENV");

        if (environment == null) {
            environment = "local";
        }

        System.out.println("Application started in environment: " +environment);
    }
}
