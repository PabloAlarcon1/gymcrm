package com.gymcrm.gymcrm.controller;

import com.gymcrm.gymcrm.service.TraineeService;
import com.gymcrm.gymcrm.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    private final TrainerService trainerService;
    private final TraineeService traineeService;

    @Autowired
    public LoginController(TrainerService trainerService, TraineeService traineeService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
    }

    @Operation(summary = "Login with username and password")
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        try {
            // Verificar las credenciales para entrenadores
            boolean trainerAuthenticated = trainerService.verifyCredentials(username, password);
            if (trainerAuthenticated) {
                // Si las credenciales del entrenador son válidas, devuelve una respuesta HTTP 200 OK
                return ResponseEntity.ok("Trainer login successful");
            } else {
                // Verificar las credenciales para trainees si las credenciales del entrenador no son válidas
                boolean traineeAuthenticated = traineeService.verifyCredentials(username, password);
                if (traineeAuthenticated) {
                    // Si las credenciales del trainee son válidas, devuelve una respuesta HTTP 200 OK
                    return ResponseEntity.ok("Trainee login successful");
                } else {
                    // Si las credenciales no son válidas para ningún tipo de usuario, devuelve una respuesta HTTP 401 Unauthorized
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                }
            }
        } catch (IllegalArgumentException e) {
            // Si ocurre algún error, devuelve un mensaje de error y un estado HTTP 400 Bad Request
            log.error("Error during login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Change password")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String userName, @RequestParam String oldPassword, @RequestParam String newPassword) {
        try {
            // Verificar si las credenciales pertenecen a un entrenador
            if (trainerService.verifyCredentials(userName, oldPassword)) {
                // Cambiar la contraseña del entrenador
                trainerService.changePassword(userName, newPassword);
                return ResponseEntity.ok("Password changed successfully for trainer");
            }
            // Verificar si las credenciales pertenecen a un trainee
            else if (traineeService.verifyCredentials(userName, oldPassword)) {
                // Cambiar la contraseña del trainee
                traineeService.changePassword(userName, newPassword);
                return ResponseEntity.ok("Password changed successfully for trainee");
            } else {
                // Si las credenciales no son válidas para ningún tipo de usuario, devuelve un estado HTTP 401 Unauthorized
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (IllegalArgumentException e) {
            // Si ocurre algún error, devuelve un mensaje de error y un estado HTTP 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}