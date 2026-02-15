package org.esrakonya.backend.common.web.config;

import lombok.Data;

@Data
public class SecurityProperties {

    /**
     * JWT related security properties
     */
    private JwtProperties jwt = new JwtProperties();
}
