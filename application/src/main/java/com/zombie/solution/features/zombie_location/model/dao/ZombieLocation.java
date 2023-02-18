package com.zombie.solution.features.zombie_location.model.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "zombie_locations")
public class ZombieLocation {

    @Id
    @Column(name = "zombie_id")
    private UUID zombieId;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private Point geom;

}
