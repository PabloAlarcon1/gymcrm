package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.model.Training;
import com.gymcrm.gymcrm.repository.TraineeRepository;
import com.gymcrm.gymcrm.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainingRepository trainingRepository;

    @Autowired
    public TraineeService(TraineeRepository traineeRepository, TrainingRepository trainingRepository) {
        this.traineeRepository = traineeRepository;
        this.trainingRepository = trainingRepository;
    }

    public Trainee saveTrainee(Trainee trainee) {
        if (traineeRepository.existsByUsername(trainee.getUser().getUserName())) {
            throw new IllegalArgumentException("Trainee with the same username already exists.");
        }
        return traineeRepository.save(trainee);
    }

    public Trainee getTraineeById(Long id) {return traineeRepository.findById(id).orElse(null);
    }

    public List<Trainee> getAllTrainees() {
        return traineeRepository.findAll();
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeRepository.save(trainee);
    }

    public void deleteTrainee(Long id) {
        Trainee traineeToDelete = traineeRepository.findById(id).orElse(null);
        if (traineeToDelete != null) {
            traineeRepository.delete(traineeToDelete);
        } else {
            throw new IllegalArgumentException("Trainee with ID " + id + " not found.");
        }
    }

    public boolean verifyCredentials(String username, String password) {
        Optional<Trainee> traineeOptional = traineeRepository.findByUserName(username);

        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            // Comparar la contraseña proporcionada con la contraseña almacenada en el Trainee
            return trainee.getUser().getPassword().equals(password);
        } else {
            // Si no se encuentra el Trainee, las credenciales no coinciden
            return false;
        }
    }

    public Trainee getTraineeByUsername(String userName) {
        Optional<Trainee> traineeOptional = traineeRepository.findByUserName(userName);
        if (traineeOptional.isPresent()) {
            return traineeOptional.get();
        } else {
            throw new IllegalArgumentException("Trainee with username " + userName + " not found.");
        }
    }

    public void changePassword(Long traineeId, String newPassword) {
        Optional<Trainee> traineeOptional = traineeRepository.findById(traineeId);
        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            trainee.getUser().setPassword(newPassword);
            traineeRepository.save(trainee); // Usar save para actualizar el trainee en la base de datos
        } else {
            throw new IllegalArgumentException("Trainee with ID " + traineeId + " not found.");
        }
    }

    public void updateTraineeProfile(Long traineeId, Trainee updatedTrainee) {
        Optional<Trainee> traineeOptional = traineeRepository.findById(traineeId);
        if (traineeOptional.isPresent()) {
            Trainee existingTrainee = traineeOptional.get();
            existingTrainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
            existingTrainee.setAddress(updatedTrainee.getAddress());
            existingTrainee.setUser(updatedTrainee.getUser());
            traineeRepository.save(existingTrainee); // Usa save para actualizar el trainee en la base de datos
        } else {
            throw new IllegalArgumentException("Trainee with ID " + traineeId + " not found.");
        }
    }

    public void activateTrainee(Long traineeId) {
        Optional<Trainee> traineeOptional = traineeRepository.findById(traineeId);
        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            trainee.getUser().setActive(true);
            traineeRepository.save(trainee); // Utiliza save para actualizar el trainee en la base de datos
        } else {
            throw new IllegalArgumentException("Trainee with ID " + traineeId + " not found.");
        }
    }

    public void deactivateTrainee(Long traineeId) {
        Optional<Trainee> traineeOptional = traineeRepository.findById(traineeId);
        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            trainee.getUser().setActive(false);
            traineeRepository.save(trainee); // Utiliza save para actualizar el trainee en la base de datos
        } else {
            throw new IllegalArgumentException("Trainee with ID " + traineeId + " not found.");
        }
    }

    public void deleteTraineeByUsername(String userName) {
        Optional<Trainee> traineeOptional = traineeRepository.findByUserName(userName);
        if (traineeOptional.isPresent()) {
            traineeRepository.delete(traineeOptional.get());
        } else {
            throw new IllegalArgumentException("Trainee with username " + userName + " not found.");
        }
    }

    public List<Training> getTrainingsByUsernameAndCriteria(String userName, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        Optional<Trainee> traineeOptional = traineeRepository.findByUserName(userName);

        Trainee trainee = traineeOptional.orElseThrow(() -> new IllegalArgumentException("Trainee with username " + userName + " not found."));

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
            return trainer.getUser().getUserName().equals(trainerName);
        }
        return false;
    }

    public Trainee updateTraineeTrainers(Long traineeId, List<Trainer> updatedTrainers) {
        Optional<Trainee> traineeOptional = traineeRepository.findById(traineeId);

        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();

            // Obtener todos los entrenamientos del trainee
            List<Training> trainings = trainee.getTrainings();

            // Actualizar los entrenamientos con los nuevos entrenadores
            for (Training training : trainings) {
                training.setTrainer((Trainer) updatedTrainers); // Actualiza el entrenador del entrenamiento
            }

            // Guardar los cambios en los entrenamientos en la base de datos
            for (Training training : trainings) {
                trainingRepository.save(training); // Guarda el entrenamiento actualizado
            }

            return trainee;
        } else {
            throw new IllegalArgumentException("Trainee with ID " + traineeId + " not found.");
        }
    }


}
