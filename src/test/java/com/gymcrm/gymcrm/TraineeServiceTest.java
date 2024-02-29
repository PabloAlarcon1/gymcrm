package com.gymcrm.gymcrm;

import com.gymcrm.gymcrm.exception.ResourceNotFoundException;
import com.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.model.User;
import com.gymcrm.gymcrm.repository.TraineeRepository;
import com.gymcrm.gymcrm.service.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getTraineeByUsername_ExistingUsername_ReturnsTrainee() {
        // Arrange
        String username = "carlosarias";
        Trainee trainee = new Trainee();
        trainee.setId(1);
        trainee.setUser(new User(username, "password"));

        when(traineeRepository.findByUserUserName(username)).thenReturn(Optional.of(trainee));

        // Act
        Trainee result = traineeService.getTraineeByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUser().getUserName());
    }

    @Test
    void getTraineeByUsername_NonExistingUsername_ThrowsException() {
        // Arrange
        String username = "nonexistinguser";

        when(traineeRepository.findByUserUserName(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> traineeService.getTraineeByUsername(username));
    }


}
