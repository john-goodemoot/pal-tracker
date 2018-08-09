package io.pivotal.pal.tracker;

import io.pivotal.pal.tracker.TimeEntryRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {

    private static final int MAX_TIME_ENTRIES = 5;
    private TimeEntryRepository repository;

    public TimeEntryHealthIndicator(TimeEntryRepository timeEntryRepository) {
        this.repository = timeEntryRepository;
    }

    public Health health() {
        Health.Builder builder = new Health.Builder();

        if(this.repository.list().size() < MAX_TIME_ENTRIES) {
            builder.up();
        } else {
            builder.down();
        }

        return builder.build();
    }
}