package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.exception.DuplicatedResourceException;
import com.gymcrm.gymcrm.exception.ResourceNotFoundException;
import com.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.model.Training;
import com.gymcrm.gymcrm.repository.TrainerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TrainerService {
    public static final String TRAINER_NOT_FOUND = "Trainer not found";

    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository) {

        this.trainerRepository = trainerRepository;
    }

    public void create(Trainer trainer) {
        log.info("Request received to create trainer");
        if (trainer.getId() != null && trainerRepository.findById(trainer.getId()).isPresent()) {
            log.info("Trainer with supplied id already exist, throwing exception");
            throw DuplicatedResourceException.builder().detailMessage("Trainer with id already exist").build();
        }
        trainerRepository.save(trainer);
        log.info("Trainer created successfully");
    }

    public Trainer saveTrainer(Trainer trainer) {
        if (trainerRepository.existsByUserUserName(trainer.getUser().getUserName())) {
            throw new IllegalArgumentException("Trainer with the same username already exists.");
        }
        return trainerRepository.save(trainer);
    }

    public Trainer get(Integer id) {
        log.info("Request received to retrieve trainer");
        Trainer trainer = trainerRepository.findById(id).orElseThrow(() -> {
            log.info("Trainer not found");
            return ResourceNotFoundException.builder().detailMessage(TRAINER_NOT_FOUND).build();
        });
        log.info("Trainer found successfully");
        return trainer;
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }


    public void update(Trainer trainer) {
        log.info("Request received to retrieve trainer");
        Optional<Trainer> trainerBD = trainerRepository.findById(trainer.getId());
        if (!trainerBD.isPresent()) {
            throw ResourceNotFoundException.builder().detailMessage(TRAINER_NOT_FOUND).build();
        }
        log.info("Trainer found successfully");
    }

    public void deleteTrainer(Integer id) {
        log.info("Request received to delete trainer");
        trainerRepository.deleteById(id);
        log.info("Trainer deleted successfully");
    }

    public boolean verifyCredentials(String userName, String password) {
        Optional<Trainer> optionalTrainer = trainerRepository.findByUserUserName(userName);

        // Verificar si se encontró un Trainer
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            // Comparar la contraseña proporcionada con la contraseña almacenada en el Trainer
            log.info("Verified credentials");
            return trainer.getUser().getPassword().equals(password);
        } else {
            log.info("Credentials do not match");
            // Si no se encuentra el Trainer, las credenciales no coinciden
            return false;
        }
    }

    public Trainer getTrainerByUsername(String userName) {
        Optional<Trainer> optionalTrainer = trainerRepository.findByUserUserName(userName);

        return optionalTrainer.orElseThrow(() -> new IllegalArgumentException("Trainer with username " + userName + " not found."));
    }

    public void changePassword(Integer trainerId, String newPassword) {
        Optional<Trainer> optionalTrainer = trainerRepository.findById(trainerId);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            trainer.getUser().setPassword(newPassword);
            trainerRepository.save(trainer);
            log.info("Password changed successfully");
        } else {
            throw new IllegalArgumentException("Trainer with ID " + trainerId + " not found.");
        }
    }

    public void updateTrainerProfile(Integer trainerId, Trainer updatedTrainer) {
        Optional<Trainer> existingTrainerOptional = trainerRepository.findById(trainerId);
        if (existingTrainerOptional.isPresent()) {
            Trainer existingTrainer = existingTrainerOptional.get();
            // Actualizar los detalles del entrenador
            existingTrainer.setSpecialization(updatedTrainer.getSpecialization());
            existingTrainer.setUser(updatedTrainer.getUser());
            trainerRepository.save(existingTrainer);
            log.info("Trainer profile updated successfully");
        } else {
            throw new IllegalArgumentException("Trainer with ID " + trainerId + " not found.");
        }
    }

    public void activateTrainer(Integer trainerId) {
        Optional<Trainer> trainerOptional = trainerRepository.findById(trainerId);
        if (trainerOptional.isPresent()) {
            Trainer trainer = trainerOptional.get();
            trainer.getUser().setActive(true);
            trainerRepository.save(trainer);
            log.info("Trainer activated successfully");
        } else {
            throw new IllegalArgumentException("Trainer with ID " + trainerId + " not found.");
        }
    }

    public void deactivateTrainer(Integer trainerId) {
        Optional<Trainer> trainerOptional = trainerRepository.findById(trainerId);
        if (trainerOptional.isPresent()) {
            Trainer trainer = trainerOptional.get();
            trainer.getUser().setActive(false);
            trainerRepository.save(trainer);
            log.info("Trainer deactivated successfully");
        } else {
            throw new IllegalArgumentException("Trainer with ID " + trainerId + " not found.");
        }
    }

    public List<Training> getTrainerTrainingsByUsernameAndCriteria(String userName, LocalDate fromDate, LocalDate toDate, String traineeName) {
        Optional<Trainer> trainerOptional = trainerRepository.findByUserUserName(userName);

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


    public List<Trainer> getTrainerByCriteria(Map<String, String> criterias) {
        if (criterias.containsKey("specializationName")) {
            return trainerRepository.getByCriteria(criterias.get("specializationName"));
        } else {
            throw new IllegalArgumentException("Criteria not supported");
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
