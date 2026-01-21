package org.esrakonya.backend.service;

import org.esrakonya.backend.dto.HealthStatusResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class HealthCheckServiceTest {

    @InjectMocks
    private HealthCheckService healthCheckService;

    @Test
    @DisplayName("Should return UP status with correct message")
    void shouldReturnCorrectHealthStatus() {
        // Arrange & Act
        HealthStatusResponse response = healthCheckService.getSystemStatus();

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals("UP", response.getStatus());
        assertEquals("Service is operating normally", response.getMessage());
        assertEquals("LOCAL", response.getEnvironment());
    }
}
