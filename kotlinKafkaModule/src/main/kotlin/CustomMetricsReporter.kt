import org.apache.kafka.common.metrics.KafkaMetric
import org.apache.kafka.common.metrics.MetricsReporter
import org.slf4j.LoggerFactory

class CustomMetricsReporter : MetricsReporter {
    private val logger = LoggerFactory.getLogger(CustomMetricsReporter::class.java)

    override fun init(metrics: MutableList<KafkaMetric>?) {
        logger.info("Initializing CustomMetricsReporter")
    }

    override fun metricChange(metric: KafkaMetric?) {
        val metricName = metric?.metricName()
        val value = metric?.value()
        logger.info("Metric: $metricName changed to value: $value")
    }

    override fun metricRemoval(metric: KafkaMetric?) {
        val metricName = metric?.metricName()
        logger.info("Metric: $metricName removed")
    }

    override fun configure(configs: MutableMap<String, *>?) {
        TODO("Not yet implemented")
    }

    override fun close() {
        logger.info("Closing CustomMetricsReporter")
    }
}