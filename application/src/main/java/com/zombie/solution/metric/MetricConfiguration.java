package com.zombie.solution.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.kafka.KafkaClientMetrics;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

import static com.zombie.solution.configuration.Micrometer.myMonitoringSystem;

@Configuration
public class MetricConfiguration {

    @Bean
    MeterRegistry registry(){
        MeterRegistry registry = myMonitoringSystem();
        Map<String, Object> configs = new HashMap<>();
        // Depending on you Kafka Cluster setup you need to configure
        // additional properties!
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        KafkaAdmin kafkaAdmin =  new KafkaAdmin(configs);
        AdminClient adminClient = AdminClient.create(configs);

        Counter.builder("requests")
                .tags("method", "GET", "outcome", "SUCCESS")
                .baseUnit("requests")
                .register(registry);

        registry.counter("http.requests.zombies", "uri", "/zombies");

        // Register kafka metrics
        new KafkaClientMetrics(adminClient).bindTo(registry);
        new JvmMemoryMetrics().bindTo(registry);
    return registry;
    }
}
