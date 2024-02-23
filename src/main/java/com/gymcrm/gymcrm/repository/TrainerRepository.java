package com.gymcrm.gymcrm.repository;

import com.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Training, Long> {

    void create(Trainer trainer);
    Optional<Trainer> get(Integer id);
    void update(Trainer trainer);
    void delete(Trainer id);
    Optional<Trainer> findByUserName(String userName);
    boolean existsByUsername(String userName);
}
