package org.esrakonya.backend.service;

import org.esrakonya.backend.dto.HealthStatusResponse;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

    public HealthStatusResponse getSystemStatus() {
        return HealthStatusResponse.builder()
                .status("UP")
                .message("Service is operating normally")
                .environment("LOCAL")
                .build();
    }
}
