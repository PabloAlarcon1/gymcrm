package com.gymcrm.gymcrm.config;

import io.micrometer.core.instrument.Counter;
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
        Counter.builder("trainers.total")
                .description("Total number of trainers")
                .register(meterRegistry);

        // Metric: Total number of trainees
        Counter.builder("trainees.total")
                .description("Total number of trainees")
                .register(meterRegistry);

        // Metric: Total number of active trainers
        Counter.builder("trainers.active")
                .description("Total number of active trainers")
                .register(meterRegistry);

        // Metric: Total number of active trainees
        Counter.builder("trainees.active")
                .description("Total number of active trainees")
                .register(meterRegistry);

        // Update counters
        updateCounters();
    }

    private void updateCounters() {
        // Update total number of trainers
        Counter trainersTotalCounter = meterRegistry.counter("trainers.total");
        trainersTotalCounter.increment(trainerService.getTotalTrainers());

        // Update total number of trainees
        Counter traineesTotalCounter = meterRegistry.counter("trainees.total");
        traineesTotalCounter.increment(traineeService.getTotalTrainees());

        // Update total number of active trainers
        Counter activeTrainersCounter = meterRegistry.counter("trainers.active");
        activeTrainersCounter.increment(trainerService.getTotalActiveTrainers());

        // Update total number of active trainees
        Counter activeTraineesCounter = meterRegistry.counter("trainees.active");
        activeTraineesCounter.increment(traineeService.getTotalActiveTrainees());
    }
}