package com.gymcrm.gymcrm.repository;

import com.gymcrm.gymcrm.gymcrm.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee> findByUserName(String userName);
    boolean existsByUsername(String userName);
}
