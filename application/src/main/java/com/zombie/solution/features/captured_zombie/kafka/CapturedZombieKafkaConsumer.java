package com.zombie.solution.features.captured_zombie.kafka;

import com.zombie.solution.features.captured_zombie.model.kafka_event.CapturedZombieEvent;
import com.zombie.solution.features.captured_zombie.service.CapturedZombieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CapturedZombieKafkaConsumer {

    @Autowired
    CapturedZombieService capturedZombieService;

    @RetryableTopic(
            kafkaTemplate = "capturedZombieEventKafkaTemplateProducer",
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 1.0),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
    @KafkaListener(topics = "captured_zombie", groupId = "zombie_application_roberto", containerFactory = "capturedZombieConcurrentKafkaListenerContainerFactory")
    private void consume(@Payload CapturedZombieEvent capturedZombieEvent,
                         @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                         @Header(KafkaHeaders.OFFSET) int offsets) {
        log.info("Received: {} from partition: {} , offset: {}", capturedZombieEvent.toString(),  partition, offsets);
        capturedZombieService.save(capturedZombieEvent);
    }
}
