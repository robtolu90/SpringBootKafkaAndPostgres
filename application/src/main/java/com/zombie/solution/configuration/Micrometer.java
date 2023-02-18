package com.zombie.solution.configuration;

import com.netflix.spectator.atlas.AtlasConfig;
import io.micrometer.atlas.AtlasMeterRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.context.annotation.Configuration;

//https://micrometer.io/docs/concepts
@Configuration
public class Micrometer {

    public static MeterRegistry myMonitoringSystem() {
        MeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry( PrometheusConfig.DEFAULT);

        CompositeMeterRegistry registry = new CompositeMeterRegistry();
        registry.add(prometheusMeterRegistry);
        // Pick a monitoring system here to use in your samples.
        return registry;
    }


}
