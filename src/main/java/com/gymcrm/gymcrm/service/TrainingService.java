package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.model.Training;
import com.gymcrm.gymcrm.repository.TraineeRepository;
import com.gymcrm.gymcrm.repository.TrainerRepository;
import com.gymcrm.gymcrm.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;

    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainingService(TrainingRepository trainingRepository, TraineeRepository traineeRepository, TrainerRepository trainerRepository) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    public Training saveTraining(Training training) {
        return trainingRepository.save(training);
    }

    public Training getTrainingById(Long id) {
        Optional<Training> optionalTraining = trainingRepository.findById(id);
        return optionalTraining.orElse(null); // Devuelve el objeto Training si está presente, o null si no lo está
    }

    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    public Training updateTraining(Training training) {
        return trainingRepository.save(training);
    }

    public void deleteTraining(Long id) {
        Optional<Training> optionalTraining = trainingRepository.findById(id);
        optionalTraining.ifPresent(training -> trainingRepository.delete(training));
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

        return trainingRepository.save(training);
    }

    public List<Training> findTrainingsByTrainerAndTrainee(Trainer trainer, Trainee trainee) {
        return trainingRepository.findTrainingsByTrainerAndTrainee(trainer, trainee);
    }

    public List<Trainer> getUnassignedTrainersByTraineeUsername(String traineeUserName) {
        // Buscar el trainee por su nombre de usuario
        Trainee trainee = traineeRepository.findByUserUserName(traineeUserName)
                .orElseThrow(() -> new IllegalArgumentException("Trainee with username " + traineeUserName + " not found."));

        // Obtener todos los entrenadores
        List<Trainer> allTrainers = trainerRepository.findAll();

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
