package org.esrakonya.backend.config;

import lombok.Data;

@Data
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
