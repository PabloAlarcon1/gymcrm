package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.model.*;
import com.gymcrm.gymcrm.repository.TraineeRepository;
import com.gymcrm.gymcrm.repository.TrainerRepository;
import com.gymcrm.gymcrm.repository.TrainingRepository;
import com.gymcrm.gymcrm.repository.UserRepository;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainingRepository trainingRepository;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final MeterRegistry meterRegistry;
    TimeLimiter timeLimiter = TimeLimiter.of(Duration.parse("saveTimeLimiter"));

    @Autowired
    public TraineeService(TraineeRepository traineeRepository, TrainingRepository trainingRepository, TrainerRepository trainerRepository, UserRepository userRepository, MeterRegistry meterRegistry) {
        this.traineeRepository = traineeRepository;
        this.trainingRepository = trainingRepository;
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.meterRegistry = meterRegistry;
    }

    /* public void create(Trainee trainee) {
        log.info("Request received to create trainer");
        if (trainee.getId() != null && traineeRepository.findById(trainee.getId()).isPresent()) {
            log.info("Trainee with supplied id already exist, throwing exception");
            throw DuplicatedResourceException.builder().detailMessage("Trainee with id already exist").build();
        }
        traineeRepository.save(trainee);
        this.meterRegistry.counter("crm.service.trainee.creation").increment();
        log.info("Trainee created successfully");
    } */

    // @TimeLimiter(name = "saveTimeLimiter")
    @io.github.resilience4j.timelimiter.annotation.TimeLimiter(name = "saveTimeLimiter")
    public User saveUser(User user) {
        if (traineeRepository.existsByUserUserName(user.getUserName())) {
            throw new IllegalArgumentException("User with the same username already exists.");
        }
        log.info("User saved successfully");
        return userRepository.save(user);
    }

    @io.github.resilience4j.timelimiter.annotation.TimeLimiter(name = "saveTimeLimiter")
    public Trainee saveTrainee(Trainee trainee) {
        if (traineeRepository.existsByUserUserName(trainee.getUser().getUserName())) {
            throw new IllegalArgumentException("Trainee with the same username already exists.");
        }
        log.info("Trainee saved successfully");
        this.meterRegistry.counter("crm.service.trainee.creation").increment();
        return traineeRepository.save(trainee);
    }


    public Trainee getTraineeById(Integer id) {
        log.info("Trainee found successfully");
        this.meterRegistry.counter("crm.service.trainee.getTraineeById").increment();
        return traineeRepository.findById(id).orElse(null);
    }

    public List<Trainee> getAllTrainees() {
        log.info("Trainees found successfully");
        this.meterRegistry.counter("crm.service.trainee.getAllTrainees").increment();
        return traineeRepository.findAll();
    }

    @io.github.resilience4j.timelimiter.annotation.TimeLimiter(name = "saveTimeLimiter")
    public Trainee updateTrainee(Trainee trainee) {
        log.info("Trainee updated successfully");
        this.meterRegistry.counter("crm.service.trainee.updateTrainee").increment();
        return traineeRepository.save(trainee);
    }

    public void deleteTrainee(Integer id) {
        Trainee traineeToDelete = traineeRepository.findById(id).orElse(null);
        if (traineeToDelete != null) {
            traineeRepository.delete(traineeToDelete);
            log.info("Trainee deleted successfully");
        } else {
            throw new IllegalArgumentException("Trainee with ID " + id + " not found.");
        }
    }

    @io.github.resilience4j.timelimiter.annotation.TimeLimiter(name = "saveTimeLimiter")
    public boolean verifyCredentials(String username, String password) {
        Optional<Trainee> traineeOptional = traineeRepository.findByUserUserName(username);

        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            // Comparar la contraseña proporcionada con la contraseña almacenada en el Trainee
            log.info("Verified credentials");
            return trainee.getUser().getPassword().equals(password);
        } else {
            // Si no se encuentra el Trainee, las credenciales no coinciden
            log.info("Credentials do not match");
            return false;
        }
    }

    public Trainee getTraineeByUsername(String userName) {
        Optional<Trainee> traineeOptional = traineeRepository.findByUserUserName(userName);
        if (traineeOptional.isPresent()) {
            log.info("Trainee found successfully");
            this.meterRegistry.counter("crm.service.trainee.getTraineeByUsername").increment();
            return traineeOptional.get();
        } else {
            throw new IllegalArgumentException("Trainee with username " + userName + " not found.");
        }
    }

    @io.github.resilience4j.timelimiter.annotation.TimeLimiter(name = "saveTimeLimiter")
    public void changePassword(String username, String newPassword) {
        Optional<Trainee> traineeOptional = traineeRepository.findByUserUserName(username);
        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            trainee.getUser().setPassword(newPassword);
            traineeRepository.save(trainee);
            log.info("Password changed successfully");
            this.meterRegistry.counter("crm.service.trainee.changedPassword").increment();
        } else {
            throw new IllegalArgumentException("Trainee with username " + username + " not found.");
        }
    }

    public void updateTraineeProfile(Integer traineeId, Trainee updatedTrainee) {
        Optional<Trainee> traineeOptional = traineeRepository.findById(traineeId);
        if (traineeOptional.isPresent()) {
            Trainee existingTrainee = traineeOptional.get();
            existingTrainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
            existingTrainee.setAddress(updatedTrainee.getAddress());
            existingTrainee.setUser(updatedTrainee.getUser());
            traineeRepository.save(existingTrainee);
            log.info("Trainee profile updated successfully");
        } else {
            throw new IllegalArgumentException("Trainee with ID " + traineeId + " not found.");
        }
    }

    public void activateTrainee(Integer traineeId) {
        Optional<Trainee> traineeOptional = traineeRepository.findById(traineeId);
        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            trainee.getUser().setActive(true);
            traineeRepository.save(trainee);
            log.info("Trainee activated successfully");
        } else {
            throw new IllegalArgumentException("Trainee with ID " + traineeId + " not found.");
        }
    }
    public void activateTraineeByUsername(String userName, boolean isActive) {
        Trainee trainee = getTraineeByUsername(userName);
        trainee.getUser().setActive(isActive);
        traineeRepository.save(trainee);
        log.info("Trainee '{}' {} successfully", userName, isActive ? "activated" : "deactivated");
    }
    public void deactivateTraineeByUsername(String userName, boolean isActive) {
        Trainee trainee = getTraineeByUsername(userName);
        trainee.getUser().setActive(isActive);
        traineeRepository.save(trainee);
        log.info("Trainee '{}' {} successfully", userName, isActive ? "activated" : "deactivated");
    }

    public void deactivateTrainee(Integer traineeId) {
        Optional<Trainee> traineeOptional = traineeRepository.findById(traineeId);
        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            trainee.getUser().setActive(false);
            traineeRepository.save(trainee);
            log.info("Trainee deactivated successfully");
        } else {
            throw new IllegalArgumentException("Trainee with ID " + traineeId + " not found.");
        }
    }

    public void deleteTraineeByUsername(String userName) {
        Optional<Trainee> traineeOptional = traineeRepository.findByUserUserName(userName);
        if (traineeOptional.isPresent()) {
            traineeRepository.delete(traineeOptional.get());
            log.info("Trainee deleted successfully");
        } else {
            throw new IllegalArgumentException("Trainee with username " + userName + " not found.");
        }
    }

    public List<Training> getTrainingsByUsernameAndCriteria(String userName, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        // Obtener el trainee por nombre de usuario
        Trainee trainee = getTraineeByUsername(userName);

        // Obtener todos los entrenamientos del trainee
        List<Training> trainings = trainee.getTrainings();

        // Filtrar los entrenamientos según los criterios proporcionados
        return trainings.stream()
                .filter(training -> isTrainingWithinDateRange(training, fromDate, toDate))
                .filter(training -> isMatchingTrainer(training, trainerName))
                .filter(training -> isMatchingTrainingType(training, trainingType))
                .collect(Collectors.toList());
    }

    private boolean isTrainingWithinDateRange(Training training, LocalDate fromDate, LocalDate toDate) {
        LocalDate trainingDate = training.getTrainingDate();
        return (fromDate == null || trainingDate.isAfter(fromDate)) &&
                (toDate == null || trainingDate.isBefore(toDate));
    }

    private boolean isMatchingTrainer(Training training, String trainerName) {
        Trainer trainer = training.getTrainer();
        return trainerName == null || trainer.getUser().getUserName().equals(trainerName);
    }

    private boolean isMatchingTrainingType(Training training, String trainingType) {
        TrainingType type = training.getTrainingType();
        return trainingType == null || type.getTrainingTypeName().equals(trainingType);
    }


    public List<Trainer> updateTraineeTrainers(String traineeUserName, List<String> trainerUserNames) {
        Trainee trainee = getTraineeByUsername(traineeUserName); // Obtener el trainee por su nombre de usuario
        List<Trainer> trainers = trainerRepository.findAllByUserNameIn(trainerUserNames); // Obtener los entrenadores por sus nombres de usuario

        trainee.setTrainers(trainers); // Establecer los nuevos entrenadores para el trainee
        traineeRepository.save(trainee); // Guardar los cambios en el trainee

        return trainers;
    }



    public List<Trainee> getTraineeByCriteria(Map<String, String> criterias) {
        return traineeRepository.getByCriteria(criterias);
    }

    public List<Trainer> getActiveUnassignedTrainers() {
        // Obtener todos los entrenadores activos
        List<Trainer> allActiveTrainers = trainerRepository.findAllByUserIsActiveTrue();

        // Obtener todos los entrenadores asignados
        List<Trainer> assignedTrainers = trainingRepository.findAll().stream()
                .map(Training::getTrainer)
                .collect(Collectors.toList());

        // Filtrar los entrenadores activos que no están asignados
        return allActiveTrainers.stream()
                .filter(trainer -> !assignedTrainers.contains(trainer))
                .collect(Collectors.toList());
    }

    public int getTotalTrainees() {
        return traineeRepository.findAll().size();
    }

    public int getTotalActiveTrainees() {
        return (int) traineeRepository.findAll().stream()
                .filter(trainee -> trainee.getUser().isActive())
                .count();
    }





}
