package org.esrakonya.backend.config;

import lombok.Data;
import org.esrakonya.backend.config.JwtProperties;

@Data
public class SecurityProperties {

    /**
     * JWT related security properties
     */
    private JwtProperties jwt = new JwtProperties();
}
