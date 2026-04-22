package org.knt.kafkatest;

import java.time.Duration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

/*
  Poll for ever until the user press ctrl-c

  Added a listener to do some destructions.
 */
public class ExiterConsumer extends AbstractKafkaConsumer{

    public static void main(String[] args) {
        System.out.println("Exiter started");
        new ExiterConsumer().start();
    }

    private void start() {
        KafkaConsumer<String, String> consumer = init();

        // Set exiter
        Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("------------------- Starting exit process --------------------------");
                consumer.wakeup();
                try {
                    // Wait to main thread finish
                    mainThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        consume(consumer);
    }

    /*
        Consume records
     */
    private void consume(KafkaConsumer<String, String> consumer) {
        try {
            // looping until ctrl-c, the shutdown hook will, cleanup on exit
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                System.out.println(System.currentTimeMillis() + "-- waiting for data...");

                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
                }
                for (TopicPartition tp: consumer.assignment())
                    System.out.println("Committing offset at position:" + consumer.position(tp));

                consumer.commitSync();
            }
        } catch (WakeupException e) {
            System.out.println("OK; going to shutdown");
            // ignore for shutdown
        } finally {
            consumer.close();
            System.out.println("Closed consumer and we are done. BYE");
        }
    }
}
