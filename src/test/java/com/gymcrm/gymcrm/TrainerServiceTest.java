package com.gymcrm.gymcrm;

import com.gymcrm.gymcrm.exception.ResourceNotFoundException;
import com.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.model.User;
import com.gymcrm.gymcrm.repository.TrainerRepository;
import com.gymcrm.gymcrm.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getTrainerByUsername_ExistingUsername_ReturnsTrainer() {
        // Arrange
        String username = "luisperez";
        Trainer trainer = new Trainer();
        trainer.setId(1);
        trainer.setUser(new User(username, "password"));

        when(trainerRepository.findByUserUserName(username)).thenReturn(Optional.of(trainer));

        // Act
        Trainer result = trainerService.getTrainerByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUser().getUserName());
    }

    @Test
    void getTrainerByUsername_NonExistingUsername_ThrowsException() {
        // Arrange
        String username = "nonexistinguser";

        when(trainerRepository.findByUserUserName(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> trainerService.getTrainerByUsername(username));
    }


}
