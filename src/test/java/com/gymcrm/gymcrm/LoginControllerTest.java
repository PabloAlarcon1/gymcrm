package com.gymcrm.gymcrm;

import com.gymcrm.gymcrm.controller.LoginController;
import com.gymcrm.gymcrm.service.TraineeService;
import com.gymcrm.gymcrm.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerService trainerService;

    @MockBean
    private TraineeService traineeService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    public void setup() {
        // Configurar comportamiento de los servicios simulados
        when(trainerService.verifyCredentials(anyString(), anyString())).thenReturn(true);
        when(traineeService.verifyCredentials(anyString(), anyString())).thenReturn(true);
    }

    @Test
    public void testLoginTrainerSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login")
                        .param("username", "trainerUsername")
                        .param("password", "trainerPassword")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Trainer login successful"));
    }

    @Test
    public void testLoginTraineeSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login")
                        .param("username", "traineeUsername")
                        .param("password", "traineePassword")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Trainee login successful"));
    }

    @Test
    public void testInvalidCredentials() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login")
                        .param("username", "invalidUsername")
                        .param("password", "invalidPassword")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("Invalid credentials"));
    }
}
