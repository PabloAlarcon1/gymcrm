package com.gymcrm.gymcrm.repository;

import com.gymcrm.gymcrm.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {

    void create(Trainee trainee);
    Optional<Trainee> getTrainee(Integer id);
    void update(Trainee trainee);
    void delete(Integer id);
    Optional<Trainee> findByUserUserName(String userName);
    boolean existsByUserUserName(String userName);
    List<Trainee> getByCriteria(Map<String, String> criterias);
}
