import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Windowed
import org.apache.kafka.streams.state.StoreBuilder
import org.apache.kafka.streams.state.Stores
import org.apache.kafka.streams.state.WindowStore
import java.time.Duration
import java.util.*


object StreamEx {

    @JvmStatic
    fun main(args: Array<String>) {
        // Set up the configuration for the Kafka Streams application
        val config = Properties().apply {
            put(StreamsConfig.APPLICATION_ID_CONFIG, "my-streamapplication")
            put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().javaClass)
            put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().javaClass)
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
        }

        val builder: StreamsBuilder = StreamsBuilder()
        val stream: KStream<String, String> = builder.stream("input-topic")

        val windowStoreSupplier: StoreBuilder<WindowStore<String, String>> =
            Stores.windowStoreBuilder(
                Stores.persistentWindowStore(
                    "window-store",
                    Duration.ofHours(5),
                    Duration.ofDays(1),
                    false
                ),
            Serdes.String(),
            Serdes.String()
        )
        
        builder.addStateStore(windowStoreSupplier)

        stream.foreach { key, value ->
            println("Stream processing $key $value")
        }

        val streams = KafkaStreams(builder.build(), config)
        streams.start()

        // Add a shutdown hook to close the streams when the application is shut down
        Runtime.getRuntime().addShutdownHook(Thread { streams.close() })
    }

}