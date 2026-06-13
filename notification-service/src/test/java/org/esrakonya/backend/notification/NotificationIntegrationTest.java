package org.esrakonya.backend.notification;

import org.esrakonya.backend.common.core.event.ProductCreatedEvent;
import org.esrakonya.backend.common.test.FullInfrastructureTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;


import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;


public class NotificationIntegrationTest extends FullInfrastructureTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @SpyBean
    private ProductNotificationConsumer notificationConsumer;

    @Test
    @DisplayName("Should consume ProductCreatedEvent and log the message")
    void shouldConsumeProductCreatedEvent() {
        ProductCreatedEvent event = ProductCreatedEvent.builder()
                .productId(101L)
                .name("Macbook Pro M3")
                .price(BigDecimal.valueOf(2500))
                .quantity(10)
                .timestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send("product-created-topic", event);

        await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    verify(notificationConsumer, atLeastOnce()).handleNotification(any(ProductCreatedEvent.class));
                });
    }
}
