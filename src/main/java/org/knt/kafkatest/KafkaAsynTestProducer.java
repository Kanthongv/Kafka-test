package org.knt.kafkatest;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Future;

public class KafkaAsynTestProducer {
    // Constants
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC = "test-topic";
    private static final String KEY_SERIALIZER = StringSerializer.class.getName();
    private static final String VALUE_SERIALIZER = StringSerializer.class.getName();
    private static final String ACKS = "all";
    private static final String ENABLE_IDEMPOTENCE = "true";
    public static void main(String[] args) {
        // Properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KEY_SERIALIZER);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER);
        props.put(ProducerConfig.ACKS_CONFIG, ACKS);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, ENABLE_IDEMPOTENCE);

        // Create a callbak class that implements the Callback interface
        class ProducerCallback implements Callback {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                System.out.printf("Message sent to topic: %s, partition: %d, offset: %d%n", recordMetadata.topic(), 
                recordMetadata.partition(), recordMetadata.offset());
                if (e != null) {
                    e.printStackTrace();
                }
            }
        }

        // Producer with the properties
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // Send
        Future<RecordMetadata> future = producer.send(new ProducerRecord<>(TOPIC, "key" + new Random().nextInt(1, 1000), "value"),
                                                      new ProducerCallback());

        // Wait for 1 second to let the message be sent
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Get the asynchronous result
        try {
            RecordMetadata recordMetadata = future.get();
            System.out.printf("Message response received asynchronously: %s, partition: %d, offset: %d%n", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
        }

        producer.close();
        System.out.println("Producer closed");
    }
}
