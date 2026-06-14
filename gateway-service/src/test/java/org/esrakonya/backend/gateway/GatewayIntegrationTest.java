package org.esrakonya.backend.gateway;

import org.esrakonya.backend.common.test.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

public class GatewayIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Gateway should be healthy and UP")
    void shouldBeHealthy() {
        webTestClient.get()
                .uri("/actuator/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("UP");
    }

    @Test
    @DisplayName("Should return 404 for non-existing routes")
    void shouldReturn404ForUnknownRoute() {
        webTestClient.get()
                .uri("/api/v1/this-path-does-not-exist")
                .exchange()
                .expectStatus().isNotFound();
    }
}
