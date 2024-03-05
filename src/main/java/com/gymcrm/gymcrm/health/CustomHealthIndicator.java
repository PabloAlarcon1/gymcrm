package com.gymcrm.gymcrm.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    public CustomHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Health health() {
        if (isDatabaseHealthy()) {
            return Health.up().withDetail("Database", "Successful connection").build();
        } else {
            return Health.down().withDetail("Database", "Unable to connect to the database").build();
        }
    }

    private boolean isDatabaseHealthy() {
        try {
            jdbcTemplate.queryForObject("SELECT 1 FROM DUAL", Integer.class);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}

