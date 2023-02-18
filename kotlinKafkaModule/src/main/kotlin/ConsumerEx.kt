import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.internals.ConsumerMetrics

import org.apache.kafka.common.metrics.JmxReporter
import org.apache.kafka.common.metrics.Measurable
import org.apache.kafka.common.metrics.MetricConfig
import org.apache.kafka.common.metrics.Metrics
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.*

object ConsumerEx {

    @JvmStatic
    fun main(args: Array<String>) {
        val listMetrics = listOf(
            "records-consumed-total",
            "records-consumed-rate",
            "incoming-byte-rate",
            "partition-assigned-latency-max",
            "partition-revoked-latency-avg",
            "records-lag-max",
            "records-lag-avg",
            "connection-count"

        )
        // Set up the Kafka consumer
        val consumerProperties = Properties().apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ConsumerConfig.GROUP_ID_CONFIG, "kotlinConsumer-new")
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
            put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000")
            put("kafka.metrics.reporters", "org.apache.kafka.common.metrics.JmxReporter1")
            put("jmx.prefix", "kafka.consumer.pippo")
            put("kafka.metrics.polling.interval.secs", "10")

        }

        val consumer = KafkaConsumer<String, String>(consumerProperties)

        val metricConfig = MetricConfig()

        val metrics = Metrics(metricConfig)
        val consumerMetrics = ConsumerMetrics("")

        //metrics.addReporter(JmxReporter())

        metrics.addReporter(CustomMetricsReporter())


        // Subscribe to the topic
        consumer.subscribe(listOf("kotlinOutput"))


        while (true) {
            // Poll for new messages from the topic
            val records = consumer.poll(Duration.ofMillis(10000))
            println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
            consumer.metrics().forEach { t, metric ->
                if(listMetrics.contains<Any?>(metric.metricName().name()))
                    println("${metric.metricName().name()}----${metric.metricValue()}----$")
            }
            consumerMetrics.fetcherMetrics.allTemplates.forEach {
                //println("${it.name()}----${it.description()}---${it.group()}---${it.tags()}--$")
            }
            // Process each record
            for (record in records) {
                println("Received record: key=${record.key()}, value=${record.value()}")
            }
        }

    }
}

