package com.gymcrm.gymcrm.repository;

import com.gymcrm.gymcrm.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

    void create(Trainer trainer);
    Optional<Trainer> get(Integer id);
    void update(Trainer trainer);
    void delete(Integer id);
    Optional<Trainer> findByUserUserName(String userName);
    boolean existsByUserUserName(String userName);
    List<Trainer> getByCriteria(Map<String, String> criterias);


}
