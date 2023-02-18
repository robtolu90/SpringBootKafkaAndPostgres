import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.time.Instant
import java.util.*

object ProducerEx {

    @JvmStatic
    fun main(args: Array<String>) {
        // Set up the Kafka producer
        val producerProperties = Properties().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        }
        val producer = KafkaProducer<String, String>(producerProperties)
        val key = "key ${Instant.now()}"
        val value =  "VALUE of $key"
        val record = ProducerRecord("topicKotlin", key, value)

        producer.send(record) { metadata, exception ->
            if (exception == null) {
                println("Message sent: key=$key, value=$value, partition=${metadata.partition()}, offset=${metadata.offset()}")
            } else {
                println("Error sending message: $exception")
            }
        }
        // Close the Kafka producer
        producer.close()
    }
}