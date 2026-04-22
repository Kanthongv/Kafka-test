package org.knt.kafkatest;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Instant;
import java.util.Properties;

public class KafkaTestProducer {

    public static void main(String[] args) throws Exception {
        String bootstrapServers = args.length > 0 ? args[0] : "localhost:9092";
        String topic = args.length > 1 ? args[1] : Constants.TOPIC;
        int messageCount = args.length > 2 ? Integer.parseInt(args[2]) : 10;

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            for (int i = 1; i <= messageCount; i++) {
                String key = "key-" + i;
                String value = "Test message " + i + " @ " + Instant.now();

                RecordMetadata metadata = producer.send(new ProducerRecord<>(topic, key, value)).get();
                System.out.printf(
                        "Sent message %d to %s [partition=%d, offset=%d]%n",
                        i,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset()
                );
            }
        }

        System.out.printf("Done. Sent %d messages to topic '%s' via %s%n", messageCount, topic, bootstrapServers);
    }
}
