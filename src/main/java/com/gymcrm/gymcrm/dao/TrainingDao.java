package com.gymcrm.gymcrm.dao;

import com.gymcrm.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.gymcrm.model.Training;

import java.util.List;

public interface TrainingDao {
    Training save(Training training);
    Training findById(Long id);
    List<Training> findAll();
    Training update(Training training);
    void delete(Long id);
}