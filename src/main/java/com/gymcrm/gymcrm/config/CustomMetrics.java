package com.gymcrm.gymcrm.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.gymcrm.gymcrm.service.TrainerService;
import com.gymcrm.gymcrm.service.TraineeService;

@Component
public class CustomMetrics {

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final MeterRegistry meterRegistry;

    @Autowired
    public CustomMetrics(TrainerService trainerService, TraineeService traineeService, MeterRegistry meterRegistry) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.meterRegistry = meterRegistry;
        registerMetrics();
    }

    private void registerMetrics() {
        // Metric: Total number of trainers
        Gauge.builder("trainers.total", trainerService::getTotalTrainers)
                .description("Total number of trainers")
                .register(meterRegistry);

        // Metric: Total number of trainees
        Gauge.builder("trainees.total", traineeService::getTotalTrainees)
                .description("Total number of trainees")
                .register(meterRegistry);

        // Metric: Total number of active trainers
        Gauge.builder("trainers.active", () -> trainerService.getTotalActiveTrainers())
                .description("Total number of active trainers")
                .register(meterRegistry);

        // Metric: Total number of active trainees
        Gauge.builder("trainees.active", () -> traineeService.getTotalActiveTrainees())
                .description("Total number of active trainees")
                .register(meterRegistry);




    }
}
