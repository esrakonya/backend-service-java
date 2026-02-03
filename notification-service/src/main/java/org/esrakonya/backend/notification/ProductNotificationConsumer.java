package org.esrakonya.backend.notification;

import lombok.extern.slf4j.Slf4j;
import org.esrakonya.backend.common.event.ProductCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductNotificationConsumer {

    @KafkaListener(topics = "product-created-topic", groupId = "notification-group")
    public void handleNotification(ProductCreatedEvent event) {
        log.info("NOTIFICATION SERVICE: Sending alert for new product: {}", event.getName());
        // Sadece bildirim mantığı burada kalmalı
    }
}