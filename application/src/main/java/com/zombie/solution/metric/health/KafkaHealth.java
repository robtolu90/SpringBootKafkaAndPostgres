package com.zombie.solution.metric.health;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
@Slf4j
@Component
public class KafkaHealth implements HealthIndicator {
    @Autowired
    AdminClient kafkaAdminClient;

    @Value("${kafka.consumer.groupId}")
    private String groupId;

    @Override
    public Health health() {
        final DescribeClusterOptions describeClusterOptions = new DescribeClusterOptions().timeoutMs(1000);
        final DescribeClusterResult describeCluster = kafkaAdminClient.describeCluster(describeClusterOptions);
        try {
            var infoLag = getConsumerGrpOffsets(groupId);
            System.out.println(infoLag);
            for (Map.Entry<TopicPartition, Long> entry : infoLag.entrySet()) {
                TopicPartition k = entry.getKey();
                Long v = entry.getValue();
                if (v > 2L) {
                    log.error("Consumer {} has lag {} ", k, v);
                    throw new KafkaHealthLagException("Consumer" +k + "has lag"+ v);
                }
            }
            final String clusterId = describeCluster.clusterId().get();

            final int nodeCount = describeCluster.nodes().get().size();
            return Health.up()
                    .withDetail("clusterId", clusterId)
                    .withDetail("nodeCount", nodeCount)
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withException(e)
                    .build();
        }
    }

    private Map<TopicPartition, Long> getConsumerGrpOffsets(String groupId)
            throws ExecutionException, InterruptedException {
        ListConsumerGroupOffsetsResult info = kafkaAdminClient.listConsumerGroupOffsets(groupId);
        Map<TopicPartition, OffsetAndMetadata> topicPartitionOffsetAndMetadataMap = info.partitionsToOffsetAndMetadata().get();

        Map<TopicPartition, Long> groupOffset = new HashMap<>();
        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : topicPartitionOffsetAndMetadataMap.entrySet()) {
            TopicPartition key = entry.getKey();
            OffsetAndMetadata metadata = entry.getValue();
            groupOffset.putIfAbsent(new TopicPartition(key.topic(), key.partition()), metadata.offset());
        }
        return groupOffset;
    }
}

