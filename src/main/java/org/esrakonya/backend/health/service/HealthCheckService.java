package org.esrakonya.backend.health.service;

import lombok.RequiredArgsConstructor;
import org.esrakonya.backend.config.AppProperties;
import org.esrakonya.backend.health.dto.HealthStatusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthCheckService {

    private final AppProperties appProperties;

    public HealthStatusResponse getSystemStatus() {
        return HealthStatusResponse.builder()
                .environment(appProperties.getEnvironment())
                .message(appProperties.getMessage())
                .status("UP")
                .build();
    }
}
