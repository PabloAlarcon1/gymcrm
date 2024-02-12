package com.gymcrm.gymcrm.repository;

import com.gymcrm.gymcrm.gymcrm.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUserName(String userName);
    boolean existsByUsername(String userName);
}
