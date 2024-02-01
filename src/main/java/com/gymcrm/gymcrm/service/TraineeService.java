package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.dao.TraineeDao;
import com.gymcrm.gymcrm.dao.TrainingDao;
import com.gymcrm.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.gymcrm.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TraineeService {

    private final TraineeDao traineeDao;
    private final TrainingDao trainingDao;

    @Autowired
    public TraineeService(TraineeDao traineeDao, TrainingDao trainingDao) {
        this.traineeDao = traineeDao;
        this.trainingDao = trainingDao;
    }

    public Trainee saveTrainee(Trainee trainee) {
        if (traineeDao.existsByUsername(trainee.getUser().getUserName())) {
            throw new IllegalArgumentException("Trainee with the same username already exists.");
        }
        return traineeDao.save(trainee);
    }

    public Trainee getTraineeById(Long id) {
        return traineeDao.findById(id);
    }

    public List<Trainee> getAllTrainees() {
        return traineeDao.findAll();
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeDao.update(trainee);
    }

    public void deleteTrainee(Long id) {
        traineeDao.delete(id);
    }

    public boolean verifyCredentials(String username, String password) {
        // Buscar el Trainee por su nombre de usuario
        Trainee trainee = traineeDao.findByUserName(username);

        // Verificar si se encontró un Trainee
        if (trainee != null) {
            // Comparar la contraseña proporcionada con la contraseña almacenada en el Trainee
            return trainee.getUser().getPassword().equals(password);
        } else {
            // Si no se encuentra el Trainee, las credenciales no coinciden
            return false;
        }
    }

    public Trainee getTraineeByUsername(String userName) {
        // Buscar el Trainer por su nombre de usuario
        return traineeDao.findByUserName(userName);
    }

    public void changePassword(Long traineeId, String newPassword) {
        Trainee trainee = traineeDao.findById(traineeId);
        if (trainee != null) {
            trainee.getUser().setPassword(newPassword);
            traineeDao.update(trainee);
        } else {
            throw new IllegalArgumentException("Trainee with ID " + traineeId + " not found.");
        }
    }

    public void updateTraineeProfile(Long traineeId, Trainee updatedTrainee) {
        Trainee existingTrainee = traineeDao.findById(traineeId);
        if (existingTrainee != null) {
            existingTrainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
            existingTrainee.setAddress(updatedTrainee.getAddress());
            existingTrainee.setUser(updatedTrainee.getUser());
            traineeDao.update(existingTrainee);
        } else {
            throw new IllegalArgumentException("Trainee with ID " + traineeId + " not found.");
        }
    }

    public void activateTrainee(Long traineeId) {
        Trainee trainee = traineeDao.findById(traineeId);
        if (trainee != null) {
            trainee.getUser().setActive(true);
            traineeDao.update(trainee);
        } else {
            throw new IllegalArgumentException("Trainee with ID " + traineeId + " not found.");
        }
    }

    public void deactivateTrainee(Long traineeId) {
        Trainee trainee = traineeDao.findById(traineeId);
        if (trainee != null) {
            trainee.getUser().setActive(false);
            traineeDao.update(trainee);
        } else {
            throw new IllegalArgumentException("Trainee with ID " + traineeId + " not found.");
        }
    }

    public void deleteTraineeByUsername(String userName) {
        Trainee trainee = traineeDao.findByUserName(userName);
        if (trainee != null) {
            traineeDao.delete(trainee.getId());
        } else {
            throw new IllegalArgumentException("Trainee with username " + userName + " not found.");
        }
    }

    public List<Training> getTrainingsByUsernameAndCriteria(String userName, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        Trainee trainee = traineeDao.findByUserName(userName);

        if (trainee == null) {
            throw new IllegalArgumentException("Trainee with username " + userName + " not found.");
        }

        List<Training> trainings = trainee.getTrainings();

        return trainings.stream()
                .filter(training -> fromDate == null || training.getTrainingDate().isAfter(fromDate))
                .filter(training -> toDate == null || training.getTrainingDate().isBefore(toDate))
                .filter(training -> trainerName == null || isMatchingTrainer(training, trainerName))
                .filter(training -> trainingType == null || training.getTrainingType().getTrainingTypeName().equals(trainingType))
                .collect(Collectors.toList());
    }

    private boolean isMatchingTrainer(Training training, String trainerName) {
        Trainer trainer = training.getTrainer();
        if (trainer != null) {
            // Check if trainer's full name matches the provided name
            return trainer.getUser().getUserName().equals(trainerName);
        }
        return false;
    }

    public Trainee updateTraineeTrainers(Long traineeId, List<Trainer> updatedTrainers) {
        Trainee trainee = traineeDao.findById(traineeId);

        if (trainee == null) {
            throw new IllegalArgumentException("Trainee with ID " + traineeId + " not found.");
        }

        // Obtener todos los entrenamientos del trainee
        List<Training> trainings = trainee.getTrainings();

        // Actualizar los entrenamientos con los nuevos entrenadores
        for (Training training : trainings) {
            training.setTrainer((Trainer) updatedTrainers); // Actualiza el entrenador del entrenamiento
        }

        // Guardar los cambios en los entrenamientos en la base de datos
        for (Training training : trainings) {
            trainingDao.update(training); // Guarda el entrenamiento actualizado
        }

        return trainee;
    }

}
