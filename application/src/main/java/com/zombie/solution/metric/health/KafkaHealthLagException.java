package com.zombie.solution.metric.health;

import io.micrometer.core.annotation.Counted;

public class KafkaHealthLagException  extends Exception {
    public KafkaHealthLagException(String errorMessage) {
        super(errorMessage);
    }
}
