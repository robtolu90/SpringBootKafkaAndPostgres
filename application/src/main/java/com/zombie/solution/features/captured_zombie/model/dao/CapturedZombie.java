package com.zombie.solution.features.captured_zombie.model.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "captured_zombie")
public class CapturedZombie {
    @Id
    @Column(name = "zombie_id")
    private UUID zombieId;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
