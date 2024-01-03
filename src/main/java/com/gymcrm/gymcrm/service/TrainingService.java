package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.dao.TrainerDao;
import com.gymcrm.gymcrm.dao.TrainingDao;
import com.gymcrm.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.gymcrm.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {

    private final TrainingDao trainingDao;

    @Autowired
    public TrainingService(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public Training saveTraining(Training training) {
        return trainingDao.save(training);
    }

    public Training getTrainingById(Long id) {
        return trainingDao.findById(id);
    }

    public List<Training> getAllTrainings() {
        return trainingDao.findAll();
    }

    public Training updateTraining(Training training) {
        return trainingDao.update(training);
    }

    public void deleteTraining(Long id) {
        trainingDao.delete(id);
    }

}
