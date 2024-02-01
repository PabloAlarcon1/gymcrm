package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.dao.TraineeDao;
import com.gymcrm.gymcrm.dao.TrainerDao;
import com.gymcrm.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.gymcrm.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainerService {

    private final TrainerDao trainerDao;

    @Autowired
    public TrainerService(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Trainer saveTrainer(Trainer trainer) {
        if (trainerDao.existsByUsername(trainer.getUser().getUserName())) {
            throw new IllegalArgumentException("Trainer with the same username already exists.");
        }
        return trainerDao.save(trainer);
    }

    public Trainer getTrainerById(Long id) {
        return trainerDao.findById(id);
    }

    public List<Trainer> getAllTrainers() {
        return trainerDao.findAll();
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerDao.update(trainer);
    }

    public void deleteTrainer(Long id) {
        trainerDao.delete(id);
    }

    public boolean verifyCredentials(String userName, String password) {
        // Buscar el Trainer por su nombre de usuario
        Trainer trainer = trainerDao.findByUserName(userName);

        // Verificar si se encontró un Trainer
        if (trainer != null) {
            // Comparar la contraseña proporcionada con la contraseña almacenada en el Trainer
            return trainer.getUser().getPassword().equals(password);
        } else {
            // Si no se encuentra el Trainer, las credenciales no coinciden
            return false;
        }
    }

    public Trainer getTrainerByUsername(String userName) {
        // Buscar el Trainer por su nombre de usuario
        return trainerDao.findByUserName(userName);
    }

    public void changePassword(Long trainerId, String newPassword) {
        Trainer trainer = trainerDao.findById(trainerId);
        if (trainer != null) {
            trainer.getUser().setPassword(newPassword);
            trainerDao.update(trainer);
        } else {
            throw new IllegalArgumentException("Trainer with ID " + trainerId + " not found.");
        }
    }

    public void updateTrainerProfile(Long trainerId, Trainer updatedTrainer) {
        Trainer existingTrainer = trainerDao.findById(trainerId);
        if (existingTrainer != null) {
            // Actualizar los detalles del entrenador
            existingTrainer.setSpecialization(updatedTrainer.getSpecialization());
            existingTrainer.setUser(updatedTrainer.getUser());
            trainerDao.update(existingTrainer);
        } else {
            throw new IllegalArgumentException("Trainer with ID " + trainerId + " not found.");
        }
    }

    public void activateTrainer(Long trainerId) {
        Trainer trainer = trainerDao.findById(trainerId);
        if (trainer != null) {
            trainer.getUser().setActive(true);
            trainerDao.update(trainer);
        } else {
            throw new IllegalArgumentException("Trainer with ID " + trainerId + " not found.");
        }
    }

    public void deactivateTrainer(Long trainerId) {
        Trainer trainer = trainerDao.findById(trainerId);
        if (trainer != null) {
            trainer.getUser().setActive(false);
            trainerDao.update(trainer);
        } else {
            throw new IllegalArgumentException("Trainer with ID " + trainerId + " not found.");
        }
    }

    public List<Training> getTrainerTrainingsByUsernameAndCriteria(String userName, LocalDate fromDate, LocalDate toDate, String traineeName) {
        Trainer trainer = trainerDao.findByUserName(userName);

        if (trainer == null) {
            throw new IllegalArgumentException("Trainer with username " + userName + " not found.");
        }

        List<Training> trainings = trainer.getTrainings();

        return trainings.stream()
                .filter(training -> fromDate == null || training.getTrainingDate().isAfter(fromDate))
                .filter(training -> toDate == null || training.getTrainingDate().isBefore(toDate))
                .filter(training -> traineeName == null || isMatchingTrainee(training, traineeName))
                .collect(Collectors.toList());
    }

    private boolean isMatchingTrainee(Training training, String traineeName) {
        Trainee trainee = training.getTrainee();
        if (trainee != null) {
            // Check if trainee's full name matches the provided name
            return trainee.getUser().getUserName().equals(traineeName);
        }
        return false;
    }

}
