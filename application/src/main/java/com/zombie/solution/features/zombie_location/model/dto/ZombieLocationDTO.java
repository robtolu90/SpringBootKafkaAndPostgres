package com.zombie.solution.features.zombie_location.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ZombieLocationDTO {
    private UUID zombieId;
    private Double latitude;
    private Double longitude;
}
