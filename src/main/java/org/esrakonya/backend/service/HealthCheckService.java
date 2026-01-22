package org.esrakonya.backend.service;

import org.esrakonya.backend.dto.HealthStatusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

    @Value("${app.environment}")
    private String environmentName;

    @Value("${app.message}")
    private String welcomeMessage;

    public HealthStatusResponse getSystemStatus() {
        return HealthStatusResponse.builder()
                .status("UP")
                .message(welcomeMessage)
                .environment(environmentName)
                .build();
    }
}
