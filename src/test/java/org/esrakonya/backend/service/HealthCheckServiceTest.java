package org.esrakonya.backend.service;

import org.esrakonya.backend.dto.HealthStatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class HealthCheckServiceTest {

    @InjectMocks
    private HealthCheckService healthCheckService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(healthCheckService, "environmentName", "TEST-ENV");
        ReflectionTestUtils.setField(healthCheckService, "welcomeMessage", "Test Message");
    }

    @Test
    void shouldReturnCorrectHealthStatus() {
        // Arrange & Act
        HealthStatusResponse response = healthCheckService.getSystemStatus();

        // Assert
        assertNotNull(response);
        assertEquals("UP", response.getStatus());
        assertEquals("Test Message", response.getMessage());
        assertEquals("TEST-ENV", response.getEnvironment());
    }
}
