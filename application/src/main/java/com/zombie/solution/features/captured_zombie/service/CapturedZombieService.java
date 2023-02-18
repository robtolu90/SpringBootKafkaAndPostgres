package com.zombie.solution.features.captured_zombie.service;

import com.zombie.solution.features.captured_zombie.model.dao.CapturedZombie;
import com.zombie.solution.features.captured_zombie.model.kafka_event.CapturedZombieEvent;
import com.zombie.solution.features.captured_zombie.repository.CapturedZombieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@Slf4j
public class CapturedZombieService {
    @Autowired
    private CapturedZombieRepository capturedZombieRepository;

    public void save(CapturedZombieEvent capturedZombieEvent) {
        log.info("Saving capturedZombieEvent: {}", capturedZombieEvent.toString());
        capturedZombieRepository.save(new CapturedZombie(capturedZombieEvent.getZombie_id(), LocalDateTime.ofInstant(capturedZombieEvent.getUpdated_at(), ZoneOffset.UTC)));
    }
}
