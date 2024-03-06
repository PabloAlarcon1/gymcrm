package com.gymcrm.gymcrm.config;

import com.gymcrm.gymcrm.service.TraineeService;
import com.gymcrm.gymcrm.service.TrainerService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

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

    }

    @Bean
    public MeterRegistry getMeterRegistry() {
        CompositeMeterRegistry meterRegistry = new CompositeMeterRegistry();
        return meterRegistry;
    }
}