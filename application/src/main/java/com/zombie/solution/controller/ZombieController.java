package com.zombie.solution.controller;

import com.zombie.solution.mapper.ZombieMapper;
import com.zombie.solution.features.zombie_location.model.dto.ZombieLocationDTO;
import com.zombie.solution.features.zombie_location.service.ZombieLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ZombieController {

    @Autowired
    private ZombieLocationService zombieLocationService;

    @GetMapping("/zombies")
    List<ZombieLocationDTO> zombies(@RequestParam Double lat, @RequestParam Double lon, @RequestParam Integer limit) {
        return zombieLocationService.findNearWithinPoint(lat, lon, limit).stream().map(ZombieMapper::zombieLocationToZombieLocationDto).collect(Collectors.toList());
    }
}