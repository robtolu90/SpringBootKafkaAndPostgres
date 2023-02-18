package com.zombie.solution.features.zombie_location.kafka;

import com.zombie.solution.features.zombie_location.model.kafka_event.ZombieLocationEvent;
import com.zombie.solution.features.zombie_location.service.ZombieLocationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
//https://www.baeldung.com/java-kafka-consumer-lag
@Component
@Slf4j
public class ZombieLocationKafkaConsumer {

    @Autowired
    ZombieLocationService zombieLocationService;

    @RetryableTopic(
            kafkaTemplate = "zombieLocationEventKafkaTemplateProducer",
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 1.0),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
    @KafkaListener(topics = "zombie_locations", groupId = "zombie_application_roberto", containerFactory = "zombieLocationConcurrentKafkaListenerContainerFactory")
    void consume(@Payload ZombieLocationEvent zombieLocationEvent,
                 @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                 @Header(KafkaHeaders.OFFSET) int offsets) {
        log.info("Received: {} from partition: {} , offset: {}", zombieLocationEvent.toString(),  partition, offsets);
        zombieLocationService.save(zombieLocationEvent);
    }

    @DltHandler
    public void multipleTopicDLT(ConsumerRecord<String, ZombieLocationEvent> message) {
        log.error("Was not possible to consume the message with offset: {} , partition: {} , body: {}", message.offset(), message.partition(), message.value().toString());
    }

}
