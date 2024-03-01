package com.gymcrm.gymcrm.controller;

import com.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.model.Training;
import com.gymcrm.gymcrm.service.TraineeService;
import com.gymcrm.gymcrm.service.TrainerService;
import com.gymcrm.gymcrm.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/trainings")
public class TrainingController {

    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Autowired
    public TrainingController(TrainingService trainingService, TraineeService traineeService, TrainerService trainerService) {
        this.trainingService = trainingService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    @Operation(summary = "Add a new training")
    @PostMapping("/trainings")
    public ResponseEntity<?> addTraining(@RequestBody TrainingRequest trainingRequest) {
        try {
            // Verificar si el trainee existe
            Trainee trainee = traineeService.getTraineeByUsername(trainingRequest.getTraineeUsername());

            // Verificar si el trainer existe
            Trainer trainer = trainerService.getTrainerByUsername(trainingRequest.getTrainerUsername());

            // Crear el objeto Training a partir de la solicitud
            Training training = new Training();
            training.setTrainingName(trainingRequest.getTrainingName());
            training.setTrainingDate(trainingRequest.getTrainingDate());
            training.setTrainingDuration(trainingRequest.getTrainingDuration());
            training.setTrainee(trainee);
            training.setTrainer(trainer);

            // Guardar el entrenamiento
            trainingService.addTraining(training);

            // Respuesta exitosa
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            // Manejar errores
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Clase para la solicitud de entrenamiento
    static class TrainingRequest {
        private String traineeUsername;
        private String trainerUsername;
        private String trainingName;
        private LocalDate trainingDate;
        private int trainingDuration;

        // getters y setters

        public String getTraineeUsername() {
            return traineeUsername;
        }

        public void setTraineeUsername(String traineeUsername) {
            this.traineeUsername = traineeUsername;
        }

        public String getTrainerUsername() {
            return trainerUsername;
        }

        public void setTrainerUsername(String trainerUsername) {
            this.trainerUsername = trainerUsername;
        }

        public String getTrainingName() {
            return trainingName;
        }

        public void setTrainingName(String trainingName) {
            this.trainingName = trainingName;
        }

        public LocalDate getTrainingDate() {
            return trainingDate;
        }

        public void setTrainingDate(LocalDate trainingDate) {
            this.trainingDate = trainingDate;
        }

        public int getTrainingDuration() {
            return trainingDuration;
        }

        public void setTrainingDuration(int trainingDuration) {
            this.trainingDuration = trainingDuration;
        }
    }

    @Operation(summary = "Get training types")
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getTrainingTypes() {
        List<Training> trainingList = trainingService.getAllTrainings();
        List<Map<String, Object>> response = trainingList.stream()
                .map(training -> {
                    Map<String, Object> typeMap = Map.of(
                            "trainingType", training.getTrainingType(),
                            "trainingTypeId", training.getId()
                    );
                    return typeMap;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
