package com.zombie.solution.features.captured_zombie.model.kafka_event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CapturedZombieEvent {
    @JsonProperty("zombie_id")
    private UUID zombie_id;
    @JsonProperty("updated_at")
    private Instant updated_at;
}
