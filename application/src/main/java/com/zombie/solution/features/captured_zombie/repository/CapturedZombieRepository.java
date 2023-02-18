package com.zombie.solution.features.captured_zombie.repository;

import com.zombie.solution.features.captured_zombie.model.dao.CapturedZombie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapturedZombieRepository extends JpaRepository<CapturedZombie, Long> { }
