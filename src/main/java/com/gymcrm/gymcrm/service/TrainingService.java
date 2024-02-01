package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.dao.TraineeDao;
import com.gymcrm.gymcrm.dao.TrainerDao;
import com.gymcrm.gymcrm.dao.TrainingDao;
import com.gymcrm.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.gymcrm.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingService {

    private final TrainingDao trainingDao;
    private final TraineeDao traineeDao;

    private final TrainerDao trainerDao;

    @Autowired
    public TrainingService(TrainingDao trainingDao, TraineeDao traineeDao, TrainerDao trainerDao) {
        this.trainingDao = trainingDao;
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
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

    public Training addTraining(Training training) {

        if (training == null) {
            throw new IllegalArgumentException("Training object cannot be null");
        }

        if (training.getTrainingName() == null || training.getTrainingName().isEmpty()) {
            throw new IllegalArgumentException("Training name cannot be null or empty");
        }

        if (training.getTrainingDate() == null) {
            throw new IllegalArgumentException("Training date cannot be null");
        }

        return trainingDao.save(training);
    }

    public List<Training> findTrainingsByTrainerAndTrainee(Trainer trainer, Trainee trainee) {
        return trainingDao.findTrainingsByTrainerAndTrainee(trainer, trainee);
    }

    public List<Trainer> getUnassignedTrainersByTraineeUsername(String traineeUserName) {
        // Buscar el trainee por su nombre de usuario
        Trainee trainee = traineeDao.findByUserName(traineeUserName);

        if (trainee == null) {
            throw new IllegalArgumentException("Trainee with username " + traineeUserName + " not found.");
        }

        // Obtener todos los entrenadores
        List<Trainer> allTrainers = trainerDao.findAll();

        // Filtrar la lista de entrenadores para excluir aquellos que están asignados al trainee
        return allTrainers.stream()
                .filter(trainer -> !isAssignedToTrainee(trainer, trainee))
                .collect(Collectors.toList());
    }

    private boolean isAssignedToTrainee(Trainer trainer, Trainee trainee) {
        for (Training training : trainer.getTrainings()) {
            if (training.getTrainee() != null && training.getTrainee().equals(trainee)) {
                return true;
            }
        }
        return false;
    }

}
