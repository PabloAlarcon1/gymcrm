package com.gymcrm.gymcrm.dao;

import com.gymcrm.gymcrm.model.Trainee;

import java.util.List;

public interface TraineeDao {
    Trainee save(Trainee trainee);
    Trainee findById(Long id);
    Trainee findByUserName(String userName);
    List<Trainee> findAll();
    Trainee update(Trainee trainee);
    void delete(Long id);
    boolean existsByUsername(String username);
}
