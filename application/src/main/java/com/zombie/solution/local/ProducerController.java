package com.zombie.solution.local;

import com.zombie.solution.features.captured_zombie.model.kafka_event.CapturedZombieEvent;
import com.zombie.solution.features.zombie_location.model.kafka_event.ZombieLocationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    @Autowired
    KafkaZombieLocationProducer kafkaZombieLocationProducer;
    @Autowired
    KafkaCapturedZombieProducer kafkaSenderCapturedZombie;


    @PostMapping("/sendZombieLocation")
    void send(@RequestBody ZombieLocationEvent request) {
        kafkaZombieLocationProducer.sendMessage(request, "zombie_locations");
    }


    @PostMapping("/sendCapturedZombie")
    void send(@RequestBody CapturedZombieEvent request ) {
        kafkaSenderCapturedZombie.sendMessage(request, "captured_zombie");
    }
}
