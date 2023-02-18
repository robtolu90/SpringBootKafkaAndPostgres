package com.zombie.solution.features.zombie_location.service;

import com.zombie.solution.features.zombie_location.model.dao.ZombieLocation;
import com.zombie.solution.features.zombie_location.model.kafka_event.ZombieLocationEvent;
import com.zombie.solution.features.zombie_location.repository.ZombieLocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.zombie.solution.util.ZombieUtil.createPointFromCoordinate;


@Service
@Slf4j
public class ZombieLocationService {

    @Autowired
    private ZombieLocationRepository zombieLocationRepository;

    public List<ZombieLocation> findNearWithinPoint(Double lon, Double lat, Integer limit) {
            return zombieLocationRepository.findNearWithinDistance(createPointFromCoordinate(lon, lat), limit);
    }

    public void save(ZombieLocationEvent zombieLocationEvent) {
        log.info("Saving: {}", zombieLocationEvent.toString());
        Point geom = createPointFromCoordinate(zombieLocationEvent.getLongitude(), zombieLocationEvent.getLatitude());
        zombieLocationRepository.save(new ZombieLocation(zombieLocationEvent.getZombie_id(), LocalDateTime.ofInstant(zombieLocationEvent.getUpdated_at(), ZoneOffset.UTC), geom));
    }
}
