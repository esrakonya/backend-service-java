package org.esrakonya.backend.common.config;

import lombok.Data;
import org.esrakonya.backend.common.config.JwtProperties;

@Data
public class SecurityProperties {

    /**
     * JWT related security properties
     */
    private JwtProperties jwt = new JwtProperties();
}
