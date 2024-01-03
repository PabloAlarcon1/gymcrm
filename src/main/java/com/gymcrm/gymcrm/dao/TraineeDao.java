package com.gymcrm.gymcrm.dao;

import com.gymcrm.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.gymcrm.model.Trainer;

import java.util.List;

public interface TraineeDao {
    Trainee save(Trainee trainee);
    Trainee findById(Long id);
    List<Trainee> findAll();
    Trainee update(Trainee trainee);
    void delete(Long id);
}
