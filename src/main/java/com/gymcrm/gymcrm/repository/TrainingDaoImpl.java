package com.gymcrm.gymcrm.repository;

import com.gymcrm.gymcrm.dao.TrainingDao;
import com.gymcrm.gymcrm.gymcrm.model.Training;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TrainingDaoImpl implements TrainingDao {

    private final Map<Long, Training> trainingMap = new HashMap<>();
    private Long trainingId = 0L;

    @Override
    public Training save(Training training) {
        if (training.getId() == null) {
            trainingId++;
            training.setId(trainingId);
        }
        trainingMap.put(training.getId(), training);
        return training;
    }

    @Override
    public Training findById(Long id) {
        return trainingMap.get(id);
    }

    @Override
    public List<Training> findAll() {
        return new ArrayList<>(trainingMap.values());
    }

    @Override
    public Training update(Training training) {
        if (trainingMap.containsKey(training.getId())) {
            trainingMap.put(training.getId(), training);
            return training;
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        trainingMap.remove(id);

    }
}
