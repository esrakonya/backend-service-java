package org.esrakonya.backend.common.web.config;

import lombok.RequiredArgsConstructor;
import org.esrakonya.backend.common.web.security.filter.JwtAuthenticationFilter;
import org.esrakonya.backend.common.web.security.service.JwtTokenService;
import org.esrakonya.backend.common.web.security.service.TokenService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({JwtProperties.class, AppProperties.class, SecurityProperties.class})
public class CommonSecurityBeanConfig {

    @Bean
    public TokenService tokenService(JwtProperties jwtProperties) {
        return new JwtTokenService(jwtProperties);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(TokenService tokenService) {
        return new JwtAuthenticationFilter(tokenService);
    }
}
