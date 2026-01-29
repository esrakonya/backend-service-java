package org.esrakonya.backend.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.esrakonya.backend.common.event.ProductCreatedEvent;
import org.esrakonya.backend.inventory.service.InventoryService;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductNotificationConsumer {

    private final InventoryService inventoryService;

    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(delay = 2000, multiplier = 2.0),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(
            topics = "product-created-topic",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleProductCreatedEvent(ProductCreatedEvent event) {

        try {
            log.info("Consumer attempting to initialize inventory for Product ID: {}", event.getProductId());
            inventoryService.initializeInventory(event.getProductId(), event.getQuantity());

            log.info(">>>> [EVENT PROCESSED SUCCESSFULLY] <<<<");
        } catch (Exception e) {
            log.error("Error processing ProductCreatedEvent for ID: {}", event.getProductId(), e);
        }


    }

    @DltHandler
    public void handleDlt(ProductCreatedEvent event, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error(">>>> [ALARM] MESSAGE SENT TO DLT! TOPIC: {}, PRODUCT ID: {} <<<<", topic, event.getProductId());
    }

}
