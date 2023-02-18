package com.zombie.solution.configuration;

import com.zombie.solution.features.captured_zombie.model.kafka_event.CapturedZombieEvent;
import com.zombie.solution.features.zombie_location.model.kafka_event.ZombieLocationEvent;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class where are created the consumer for the 2 topics and the template
 * for the producers used in the error handler
 */
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public AdminClient kafkaAdminClient() {
        return AdminClient.create(getConfig());
    }

    @Autowired
    MeterRegistry registry;

    @Value("${kafka.consumer.groupId}")
    private String groupId;

    @Autowired
    KafkaTemplate<String, ZombieLocationEvent> zombieLocationEventKafkaTemplateProducer;

    @Autowired
    KafkaTemplate<String, CapturedZombieEvent> capturedZombieEventKafkaTemplateProducer;

    @Bean
    ConsumerFactory<String, CapturedZombieEvent> capturedZombieConsumerFactory()  {
        return new DefaultKafkaConsumerFactory<>(
                getConfig(),
                new StringDeserializer(),
                new JsonDeserializer<>(CapturedZombieEvent.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CapturedZombieEvent> capturedZombieConcurrentKafkaListenerContainerFactory()
    {
        ConcurrentKafkaListenerContainerFactory<String, CapturedZombieEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(capturedZombieConsumerFactory());
        factory.setCommonErrorHandler(retryErrorHandler(capturedZombieEventKafkaTemplateProducer));
        return factory;
    }

    @Bean
    ConsumerFactory<String, ZombieLocationEvent> zombieLocationConsumerFactory()  {
        DefaultKafkaConsumerFactory<String, ZombieLocationEvent> df = new DefaultKafkaConsumerFactory<>(
                getConfig(),
                new StringDeserializer(),
                new JsonDeserializer<>(ZombieLocationEvent.class)
        );

        df.addListener(new MicrometerConsumerListener<>(registry));
        return df;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ZombieLocationEvent> zombieLocationConcurrentKafkaListenerContainerFactory()
    {
        ConcurrentKafkaListenerContainerFactory<String, ZombieLocationEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(zombieLocationConsumerFactory());
        factory.setCommonErrorHandler(retryErrorHandler(zombieLocationEventKafkaTemplateProducer));
        return factory;
    }

    public DefaultErrorHandler retryErrorHandler(KafkaTemplate<? ,? > kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1000, 3));
    }


    private Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return config;
    }
}
