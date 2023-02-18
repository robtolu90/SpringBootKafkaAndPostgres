package com.zombie.solution.features.zombie_location.repository;

import com.zombie.solution.features.zombie_location.model.dao.ZombieLocation;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZombieLocationRepository extends JpaRepository<ZombieLocation, Long> {

    @Query(value = "SELECT * FROM zombie_locations z ORDER BY ST_Distance(z.geom, :point ) LIMIT :limit", nativeQuery = true)
    List<ZombieLocation> findNearWithinDistance(@Param("point") Point point, @Param("limit") Integer limit);

}
