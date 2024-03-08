package com.gymcrm.gymcrm.controller;


import com.gymcrm.gymcrm.exception.ResourceNotFoundException;
import com.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.model.Training;
import com.gymcrm.gymcrm.model.User;
import com.gymcrm.gymcrm.service.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trainees")
public class TraineeController {

    private final TraineeService traineeService;
    private PasswordEncoder passwordEncoder = null;



    @Autowired
    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
        this.passwordEncoder = passwordEncoder;
    }



    @Operation(summary = "Register a new trainee")
    @PostMapping
    public ResponseEntity<Trainee> registerTrainee(@RequestBody TraineeRegistrationRequest request) {
        // Validar si los campos requeridos están presentes en la solicitud
        if (request.getFirstName() == null || request.getLastName() == null || request.getUserName() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Crear un nuevo usuario
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserName(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);

        // Guardar el usuario en la base de datos
        User savedUser = traineeService.saveUser(user);

        // Crear un nuevo Trainee y asociarlo al usuario creado
        Trainee trainee = new Trainee();
        trainee.setUser(savedUser);
        trainee.setDateOfBirth(request.getDateOfBirth());
        trainee.setAddress(request.getAddress());

        // Guardar el Trainee en la base de datos
        Trainee savedTrainee = traineeService.saveTrainee(trainee);

        // Devolver la respuesta con el Trainee creado
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrainee);
    }

    // Clase para representar la solicitud de registro de Trainee
    @Setter
    @Getter
    public static class TraineeRegistrationRequest {
        private String firstName;
        private String lastName;
        private String userName;
        private String password;
        private LocalDate dateOfBirth;
        private String address;

        public TraineeRegistrationRequest(String firstName, String lastName, String userName, String password, LocalDate dateOfBirth, String address) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.userName = userName;
            this.password = password;
            this.dateOfBirth = dateOfBirth;
            this.address = address;
        }

        public TraineeRegistrationRequest() {

        }
    }

    @Operation(summary = "Get trainee profile by username")
    @GetMapping("/profile")
    public ResponseEntity<?> getTraineeProfile(@RequestParam String userName) {
        try {
            Trainee trainee = traineeService.getTraineeByUsername(userName);
            Map<String, Object> profile = new HashMap<>();
            profile.put("firstName", trainee.getUser().getFirstName());
            profile.put("lastName", trainee.getUser().getLastName());
            profile.put("dateOfBirth", trainee.getDateOfBirth());
            profile.put("address", trainee.getAddress());
            profile.put("isActive", trainee.getUser().isActive());
            List<Map<String, String>> trainersList = trainee.getTrainings().stream()
                    .map(training -> {
                        Map<String, String> trainerMap = new HashMap<>();
                        Trainer trainer = training.getTrainer();
                        if (trainer != null) {
                            trainerMap.put("username", trainer.getUser().getUserName());
                            trainerMap.put("firstName", trainer.getUser().getFirstName());
                            trainerMap.put("lastName", trainer.getUser().getLastName());
                            trainerMap.put("specialization", trainer.getSpecialization().getName());
                        } else {
                            // Aquí puedes manejar el caso donde no hay un entrenador asociado al entrenamiento
                            // Por ejemplo, puedes poner valores predeterminados o un mensaje indicando que no hay entrenador
                            trainerMap.put("message", "No trainer associated");
                        }
                        return trainerMap;
                    })
                    .collect(Collectors.toList());
            profile.put("trainersList", trainersList);
            return ResponseEntity.ok(profile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Update trainee profile by ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTraineeProfile(@PathVariable Integer id, @RequestBody TraineeUpdateRequest request) {
        try {
            // Verificar si el trainee existe
            Trainee existingTrainee = traineeService.getTraineeById(id);

            // Actualizar los campos del trainee con los nuevos valores proporcionados en la solicitud
            existingTrainee.getUser().setFirstName(request.getFirstName());
            existingTrainee.getUser().setLastName(request.getLastName());
            existingTrainee.setDateOfBirth(request.getDateOfBirth());
            existingTrainee.setAddress(request.getAddress());
            existingTrainee.getUser().setActive(request.isActive());

            // Guardar los cambios en la base de datos
            traineeService.updateTrainee(existingTrainee);

            // Devolver la respuesta con el trainee actualizado
            return ResponseEntity.ok(existingTrainee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Clase para representar la solicitud de actualización del perfil de Trainee
    @Setter
    @Getter
    static class TraineeUpdateRequest {
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;
        private String address;
        private boolean active;

        // Getters y setters

    }

    @Operation(summary = "Delete trainee profile by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTraineeProfile(@PathVariable Integer id) {
        try {
            traineeService.deleteTrainee(id);
            return ResponseEntity.ok().build(); // Retorna un código de estado 200 OK si la eliminación es exitosa
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna un código de estado 404 Not Found si el Trainee no se encuentra
        }
    }

    @Operation(summary = "Get active unassigned trainers")
    @GetMapping("/unassigned-trainers")
    public ResponseEntity<List<Trainer>> getActiveUnassignedTrainers() {
        List<Trainer> unassignedTrainers = traineeService.getActiveUnassignedTrainers();
        return ResponseEntity.ok(unassignedTrainers);
    }

    @Operation(summary = "Update trainee trainers")
    @PutMapping("/{traineeUserName}/trainers")
    public ResponseEntity<?> updateTraineeTrainers(@PathVariable String traineeUserName, @RequestBody List<String> trainerUserNames) {
        try {
            List<Trainer> updatedTrainers = traineeService.updateTraineeTrainers(traineeUserName, trainerUserNames);
            return ResponseEntity.ok(updatedTrainers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Get trainee training list")
    @GetMapping("/trainings")
    public ResponseEntity<?> getTraineeTrainingsList(
            @RequestParam String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingType
    ) {
        try {
            List<Training> trainings = traineeService.getTrainingsByUsernameAndCriteria(username, periodFrom, periodTo, trainerName, trainingType);
            // Convertir la lista de entrenamientos en una lista de mapas
            List<Map<String, Object>> trainingDetailsList = trainings.stream()
                    .map(training -> {
                        Map<String, Object> trainingDetails = new HashMap<>();
                        trainingDetails.put("name", training.getTrainingName());
                        trainingDetails.put("date", training.getTrainingDate());
                        trainingDetails.put("type", training.getTrainingType().getTrainingTypeName());
                        trainingDetails.put("duration", training.getTrainingDuration());
                        trainingDetails.put("trainer", training.getTrainer().getUser().getUserName());
                        return trainingDetails;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(trainingDetailsList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Activate trainee")
    @PatchMapping("/{username}/activate")
    public ResponseEntity<?> activateTrainee(@PathVariable String username, @RequestParam boolean isActive) {
        try {
            traineeService.activateTraineeByUsername(username, isActive);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Deactivate trainee")
    @PatchMapping("/{username}/deactivate")
    public ResponseEntity<?> deactivateTrainee(@PathVariable String username, @RequestParam boolean isActive) {
        try {
            traineeService.deactivateTraineeByUsername(username, isActive);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}