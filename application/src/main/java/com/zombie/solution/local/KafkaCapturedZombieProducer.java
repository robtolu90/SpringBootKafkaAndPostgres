package com.zombie.solution.local;


import com.zombie.solution.features.captured_zombie.model.kafka_event.CapturedZombieEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaCapturedZombieProducer {

    @Autowired
    KafkaTemplate<String, CapturedZombieEvent> capturedZombieEventKafkaTemplateProducer;

    void sendMessage(CapturedZombieEvent message,String topicName) {
        capturedZombieEventKafkaTemplateProducer.send(topicName, message);
    }
}
