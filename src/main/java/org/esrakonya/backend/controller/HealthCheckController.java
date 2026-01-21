package org.esrakonya.backend.controller;

import lombok.RequiredArgsConstructor;
import org.esrakonya.backend.dto.HealthStatusResponse;
import org.esrakonya.backend.service.HealthCheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller to verify the service status.
 */
@RestController
@RequiredArgsConstructor
public class HealthCheckController {
    private final HealthCheckService healthCheckService;

    @GetMapping("/health")
    public HealthStatusResponse getHealthStatus() {
        return healthCheckService.getSystemStatus();
    }

}
