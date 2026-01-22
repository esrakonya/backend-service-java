package org.esrakonya.backend.health;

import lombok.RequiredArgsConstructor;
import org.esrakonya.backend.health.dto.HealthStatusResponse;
import org.esrakonya.backend.health.HealthCheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
