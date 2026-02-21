package org.esrakonya.backend.common.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtProperties {

    /**
     * Secret key used for signing JWT tokens
     */
    private String secret;

    /**
     * Token expiration time in milliseconds
     */
    private long expiration;

    /**
     * JWT issuer
     */
    private String issuer = "esrakonya-backend";
}
