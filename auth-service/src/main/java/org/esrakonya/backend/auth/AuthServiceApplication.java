package org.esrakonya.backend.auth;


import org.esrakonya.backend.common.web.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.esrakonya.backend")
@EnableConfigurationProperties(AppProperties.class)
@EntityScan(basePackages = {
        "org.esrakonya.backend.auth.domain",
        "org.esrakonya.backend.common.persistence.model"
})
@EnableJpaRepositories(basePackages = "org.esrakonya.backend.auth.repository")
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}