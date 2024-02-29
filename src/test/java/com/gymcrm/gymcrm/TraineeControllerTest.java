package com.gymcrm.gymcrm;

import com.gymcrm.gymcrm.controller.TraineeController;
import com.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.model.User;
import com.gymcrm.gymcrm.service.TraineeService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TraineeControllerTest {

    @Test
    public void testRegisterTrainee() {
        // Mock del servicio de Trainee
        TraineeService traineeService = mock(TraineeService.class);

        // Crear instancia de TraineeController con el servicio mockeado
        TraineeController traineeController = new TraineeController(traineeService);

        // Datos de prueba para el Trainee
        TraineeController.TraineeRegistrationRequest request = new TraineeController.TraineeRegistrationRequest();
        request.setFirstName("carlos");
        request.setLastName("arias");
        request.setUserName("carlosarias");
        request.setPassword("password");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setAddress("123 Street");

        // Mock del Trainee creado
        Trainee savedTrainee = new Trainee();
        savedTrainee.setId(1); // Suponiendo que la base de datos asigna un ID 1
        savedTrainee.setDateOfBirth(request.getDateOfBirth());
        savedTrainee.setAddress(request.getAddress());

        // Mock del servicio para devolver el Trainee guardado
        when(traineeService.saveUser(any())).thenReturn(new User()); // Suponiendo que el método devuelve un nuevo User
        when(traineeService.saveTrainee(any())).thenReturn(savedTrainee);

        // Llamada al método del controlador que queremos probar
        ResponseEntity<Trainee> response = traineeController.registerTrainee(request);

        // Verificar si se creó correctamente el Trainee
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedTrainee, response.getBody());
    }


}
