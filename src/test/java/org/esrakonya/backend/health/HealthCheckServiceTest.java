package org.esrakonya.backend.health;

import org.esrakonya.backend.config.AppProperties;
import org.esrakonya.backend.health.dto.HealthStatusResponse;
import org.esrakonya.backend.health.service.HealthCheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HealthCheckServiceTest {

    @Mock
    private AppProperties appProperties;

    @InjectMocks
    private HealthCheckService healthCheckService;

    @Test
    void shouldReturnCorrectHealthStatus() {
        // Arrange
        when(appProperties.getEnvironment()).thenReturn("test-env");
        when(appProperties.getMessage()).thenReturn("Test Message");

        // Act
        HealthStatusResponse response = healthCheckService.getSystemStatus();

        // Assert
        assertEquals("UP", response.getStatus());
        assertEquals("test-env", response.getEnvironment());
        assertEquals("Test Message", response.getMessage());
    }
}
