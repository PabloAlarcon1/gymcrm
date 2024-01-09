package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.dao.TrainerDao;
import com.gymcrm.gymcrm.dao.TrainingDao;
import com.gymcrm.gymcrm.exceptions.NotFoundException;
import com.gymcrm.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.gymcrm.model.Training;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainingService {

    private final TrainingDao trainingDao;
    private final TraineeService traineeService;






    public Training saveTraining(Training training) {
        if (training.getTrainee().getId() == null || traineeService.getTraineeById(training.getTrainee().getId()) == null) {
            log.info("Trainee not valid for training");
            throw new NotFoundException("Trainee not found");
        }
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
