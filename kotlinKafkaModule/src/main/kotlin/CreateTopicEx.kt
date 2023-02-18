import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import java.util.*

object CreateTopicEx {
    @JvmStatic
    fun main(args: Array<String>) {
            // Set up the configuration for the Kafka Admin Client
            val config = Properties().apply {
                put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            }

            // Create the Kafka Admin Client
            val adminClient = AdminClient.create(config)

            // Define the topic to create
            val topicName = "kotlinOutput"
            val topicConfig = NewTopic(topicName, 1, 1.toShort())

            // Check if the topic already exists
            val topics: MutableSet<String> = adminClient.listTopics().names().get()
            topics.forEach{ println("Topic existing "+it) }
            if (!topics.contains(topicName)) {
                // Create the topic if it doesn't exist
                adminClient.createTopics(listOf(topicConfig)).all().get()
                println("Topic created: $topicName")
            } else {
                println("Topic already exists: $topicName")
            }

            // Close the Kafka Admin Client
            adminClient.close()
        }
}