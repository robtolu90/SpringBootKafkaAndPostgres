package com.zombie.solution.local;

import com.zombie.solution.features.zombie_location.model.kafka_event.ZombieLocationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaZombieLocationProducer {

    @Autowired
    KafkaTemplate<String, ZombieLocationEvent> zombieLocationEventKafkaTemplateProducer;

    void sendMessage(ZombieLocationEvent message , String topicName) {
        log.info("Sending: {}", message.toString());
        zombieLocationEventKafkaTemplateProducer.send(topicName, message);
    }
}
