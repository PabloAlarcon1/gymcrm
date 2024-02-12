package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.gymcrm.model.Training;
import com.gymcrm.gymcrm.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository) {

        this.trainerRepository = trainerRepository;
    }

    public Trainer saveTrainer(Trainer trainer) {
        if (trainerRepository.existsByUsername(trainer.getUser().getUserName())) {
            throw new IllegalArgumentException("Trainer with the same username already exists.");
        }
        return trainerRepository.save(trainer);
    }

    public Trainer getTrainerById(Long id) {
        Optional<Trainer> optionalTrainer = trainerRepository.findById(id);
        if (optionalTrainer.isPresent()) {
            return optionalTrainer.get();
        } else {
            throw new IllegalArgumentException("Trainer with ID " + id + " not found.");
        }
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public void deleteTrainer(Long id) {
        Optional<Trainer> optionalTrainer = trainerRepository.findById(id);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            trainerRepository.delete(trainer);
        } else {
            throw new IllegalArgumentException("Trainer with ID " + id + " not found.");
        }
    }

    public boolean verifyCredentials(String userName, String password) {
        Optional<Trainer> optionalTrainer = trainerRepository.findByUserName(userName);

        // Verificar si se encontró un Trainer
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            // Comparar la contraseña proporcionada con la contraseña almacenada en el Trainer
            return trainer.getUser().getPassword().equals(password);
        } else {
            // Si no se encuentra el Trainer, las credenciales no coinciden
            return false;
        }
    }

    public Trainer getTrainerByUsername(String userName) {
        Optional<Trainer> optionalTrainer = trainerRepository.findByUserName(userName);

        return optionalTrainer.orElseThrow(() -> new IllegalArgumentException("Trainer with username " + userName + " not found."));
    }

    public void changePassword(Long trainerId, String newPassword) {
        Optional<Trainer> optionalTrainer = trainerRepository.findById(trainerId);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            trainer.getUser().setPassword(newPassword);
            trainerRepository.save(trainer);
        } else {
            throw new IllegalArgumentException("Trainer with ID " + trainerId + " not found.");
        }
    }

    public void updateTrainerProfile(Long trainerId, Trainer updatedTrainer) {
        Optional<Trainer> existingTrainerOptional = trainerRepository.findById(trainerId);
        if (existingTrainerOptional.isPresent()) {
            Trainer existingTrainer = existingTrainerOptional.get();
            // Actualizar los detalles del entrenador
            existingTrainer.setSpecialization(updatedTrainer.getSpecialization());
            existingTrainer.setUser(updatedTrainer.getUser());
            trainerRepository.save(existingTrainer); // Guardar los cambios en la base de datos
        } else {
            throw new IllegalArgumentException("Trainer with ID " + trainerId + " not found.");
        }
    }

    public void activateTrainer(Long trainerId) {
        Optional<Trainer> trainerOptional = trainerRepository.findById(trainerId);
        if (trainerOptional.isPresent()) {
            Trainer trainer = trainerOptional.get();
            trainer.getUser().setActive(true);
            trainerRepository.save(trainer); // Guardar los cambios en la base de datos
        } else {
            throw new IllegalArgumentException("Trainer with ID " + trainerId + " not found.");
        }
    }

    public void deactivateTrainer(Long trainerId) {
        Optional<Trainer> trainerOptional = trainerRepository.findById(trainerId);
        if (trainerOptional.isPresent()) {
            Trainer trainer = trainerOptional.get();
            trainer.getUser().setActive(false);
            trainerRepository.save(trainer); // Guarda los cambios en la base de datos
        } else {
            throw new IllegalArgumentException("Trainer with ID " + trainerId + " not found.");
        }
    }

    public List<Training> getTrainerTrainingsByUsernameAndCriteria(String userName, LocalDate fromDate, LocalDate toDate, String traineeName) {
        Optional<Trainer> trainerOptional = trainerRepository.findByUserName(userName);

        if (trainerOptional.isPresent()) {
            Trainer trainer = trainerOptional.get();

            List<Training> trainings = trainer.getTrainings();

            return trainings.stream()
                    .filter(training -> fromDate == null || training.getTrainingDate().isAfter(fromDate))
                    .filter(training -> toDate == null || training.getTrainingDate().isBefore(toDate))
                    .filter(training -> traineeName == null || isMatchingTrainee(training, traineeName))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Trainer with username " + userName + " not found.");
        }
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
