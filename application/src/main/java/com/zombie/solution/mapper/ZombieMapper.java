package com.zombie.solution.mapper;

import com.zombie.solution.features.zombie_location.model.dao.ZombieLocation;
import com.zombie.solution.features.zombie_location.model.dto.ZombieLocationDTO;
import lombok.experimental.UtilityClass;


@UtilityClass
public class ZombieMapper {
    /**
     * Convert the ZombieLocationDAO object from the database to a ZombieLocationDTO that
     * will be returned by the endpoint
     *
     * @param zombieLocation from database
     * @return Dto object used as response of the endpoint
     */
    public static ZombieLocationDTO zombieLocationToZombieLocationDto(ZombieLocation zombieLocation) {
        return new ZombieLocationDTO(zombieLocation.getZombieId(), zombieLocation.getGeom().getY(), zombieLocation.getGeom().getX());
    }
}
