package org.esrakonya.backend.health.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Health Status.
 * Using Lombok to eliminate boilerplate code.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthStatusResponse {
    private String status;
    private String message;
    private String environment;
}
