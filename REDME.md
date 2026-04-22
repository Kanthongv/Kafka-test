# Kafka Test
This is a test project for Apache Kafka.
I want to test all the features and the many parameters that exist.


## Prerequisites
Start the Kafka broker using the docker-compose.yml file.
```bash
docker-compose up -d
```
> ![Warning](https://img.shields.io/badge/Warning-yellow?style=for-the-badge)
>
> You must stop the broker after the tests.
> ```bash
> docker-compose down
> ```

## Kafka Test Producer

```bash
mvn exec:java -Dexec.mainClass="org.knt.kafkatest.KafkaTestProducer" -Dexec.args="localhost:9092 test-topic 10"
```

## Kafka Test Consumer

```bash
mvn exec:java -Dexec.mainClass="org.knt.kafkatest.KafkaTestConsumer" -Dexec.args="localhost:9092 test-topic 10"
```

## Kafka Asynchronous Test Producer

```bash
mvn exec:java -Dexec.mainClass="org.knt.kafkatest.KafkaAsynTestProducer"
```

## Kafka infinite consumer
Wait ctrl-c from user to stop consuming, but before that it do some task like:
- print some logs in a callback pushed *Thread*

```bash
mvn exec:java -Dexec.mainClass="org.knt.kafkatest.ExiterConsumer"
```