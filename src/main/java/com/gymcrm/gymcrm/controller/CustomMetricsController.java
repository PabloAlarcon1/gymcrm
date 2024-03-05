package com.gymcrm.gymcrm.controller;

import com.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.model.Training;
import com.gymcrm.gymcrm.service.TraineeService;
import com.gymcrm.gymcrm.service.TrainerService;
import com.gymcrm.gymcrm.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomMetricsController {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingService trainingService;

    @GetMapping("/custom-metrics")
    public String customMetrics() {
        // Recopilación de métricas personalizadas
        List<Trainee> trainees = traineeService.getAllTrainees();
        List<Trainer> trainers = trainerService.getAllTrainers();
        List<Training> trainings = trainingService.getAllTrainings();

        // Construcción de métricas personalizadas
        StringBuilder metricsBuilder = new StringBuilder();
        metricsBuilder.append("# HELP custom_trainees_count The number of trainees\n");
        metricsBuilder.append("# TYPE custom_trainees_count gauge\n");
        metricsBuilder.append("custom_trainees_count " + trainees.size() + "\n");

        metricsBuilder.append("# HELP custom_trainers_count The number of trainers\n");
        metricsBuilder.append("# TYPE custom_trainers_count gauge\n");
        metricsBuilder.append("custom_trainers_count " + trainers.size() + "\n");

        metricsBuilder.append("# HELP custom_trainings_count The number of trainings\n");
        metricsBuilder.append("# TYPE custom_trainings_count gauge\n");
        metricsBuilder.append("custom_trainings_count " + trainings.size() + "\n");



        return metricsBuilder.toString();
    }
}
