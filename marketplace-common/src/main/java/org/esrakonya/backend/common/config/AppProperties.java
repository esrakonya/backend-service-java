package org.esrakonya.backend.common.config;

import lombok.Data;
import org.esrakonya.backend.common.config.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String environment = "dev";
    private String message = "Marketplace API";

    /**
     * Security related properties
     */
    private SecurityProperties security = new SecurityProperties();
}
