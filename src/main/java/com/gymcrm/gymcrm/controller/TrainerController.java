package com.gymcrm.gymcrm.controller;

import com.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/trainers")
public class TrainerController {

    private final TrainerService trainerService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TrainerController(TrainerService trainerService, PasswordEncoder passwordEncoder) {
        this.trainerService = trainerService;
        this.passwordEncoder = passwordEncoder;
    }


    @Operation(summary = "Register a new trainer")
    @PostMapping("/register")
    public ResponseEntity<String> registerTrainer(@RequestBody Trainer trainer) {
        try {
            // Encripta la contraseña antes de guardarla
            String encodedPassword = passwordEncoder.encode(trainer.getUser().getPassword());
            trainer.getUser().setPassword(encodedPassword);

            // Llama al método del servicio para guardar al entrenador
            Trainer savedTrainer = trainerService.saveTrainer(trainer);
            // Devuelve el nombre de usuario y contraseña del entrenador recién registrado
            String response = "UserName: " + savedTrainer.getUser().getUserName() + "\nPassword: " + savedTrainer.getUser().getPassword();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Si ocurre algún error, devuelve un mensaje de error y un estado HTTP 400 Bad Request
            log.error("Error registering trainer: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Get trainer profile by username")
    @GetMapping("/{userName}")
    public ResponseEntity<Map<String, Object>> getTrainerProfile(@PathVariable String userName) {
        Trainer trainer = trainerService.getTrainerByUsername(userName);

        // Preparar la respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("firstName", trainer.getUser().getFirstName());
        response.put("lastName", trainer.getUser().getLastName());
        response.put("specialization", trainer.getSpecialization().getName());
        response.put("isActive", trainer.getUser().isActive());

        // Obtener la lista de trainees
        List<Map<String, String>> traineesList = trainer.getTrainings().stream()
                .map(training -> {
                    Map<String, String> traineeMap = new HashMap<>();
                    traineeMap.put("username", training.getTrainee().getUser().getUserName());
                    traineeMap.put("firstName", training.getTrainee().getUser().getFirstName());
                    traineeMap.put("lastName", training.getTrainee().getUser().getLastName());
                    return traineeMap;
                })
                .collect(Collectors.toList());
        response.put("traineesList", traineesList);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update trainer profile")
    @PutMapping("/{userName}")
    public ResponseEntity<String> updateTrainerProfile(@PathVariable String username, @RequestBody Trainer updatedTrainer) {
        try {
            // Obtener el entrenador existente
            Trainer existingTrainer = trainerService.getTrainerByUsername(username);

            // Actualizar los detalles del perfil del entrenador
            existingTrainer.getUser().setFirstName(updatedTrainer.getUser().getFirstName());
            existingTrainer.getUser().setLastName(updatedTrainer.getUser().getLastName());
            existingTrainer.getUser().setActive(updatedTrainer.getUser().isActive());

            // Actualizar la especialización del entrenador
            existingTrainer.setSpecialization(updatedTrainer.getSpecialization());

            // Guardar el entrenador actualizado
            trainerService.updateTrainerProfile(existingTrainer.getId(), existingTrainer);

            // Devolver una respuesta exitosa
            return ResponseEntity.ok("Trainer profile updated successfully");

        } catch (IllegalArgumentException e) {
            // Si ocurre algún error, devolver un mensaje de error y un estado HTTP 400 Bad Request
            log.error("Error updating trainer profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Activate trainer")
    @PatchMapping("/{username}/activate")
    public ResponseEntity<?> activateTrainer(@PathVariable String username, @RequestParam boolean isActive) {
        try {
            trainerService.activateTrainerByUsername(username, isActive);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Deactivate trainer")
    @PatchMapping("/{username}/deactivate")
    public ResponseEntity<?> deactivateTrainer(@PathVariable String username, @RequestParam boolean isActive) {
        try {
            trainerService.deactivateTrainerByUsername(username, isActive);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }




}
