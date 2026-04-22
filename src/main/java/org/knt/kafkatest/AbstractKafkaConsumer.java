package org.knt.kafkatest;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

/*
    Class for managing common Kafka task's step
 */
public abstract class AbstractKafkaConsumer {

    // Set the default params
    // Subscribe to the topic
    protected KafkaConsumer<String, String> init() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", Constants.GROUP_ID);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // Create a consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // I can subscribe to a lot of topics at se same time
        consumer.subscribe(Collections.singletonList(Constants.TOPIC));

        System.out.println("Consumer connected to: " + Constants.TOPIC);

        return consumer;
    }
}
