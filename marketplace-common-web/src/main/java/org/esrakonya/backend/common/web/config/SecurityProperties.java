package org.esrakonya.backend.common.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    /**
     * JWT related security properties
     */
    private JwtProperties jwt = new JwtProperties();
}
