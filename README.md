# KafkaProducer

This project is producing and sending messages to a Kafka Broker.

## prerequisites:
Apache Kafka installation

## run the application:

- start the zookeeper
```
    zookeeper-servestart.sh config/zookeeper.properties
```
- start kafka server
```
start kakfa: kafka-server-start.sh config/server.properties
```
- create the topic
```
kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic first_topic --create --partitions 3 --replication-factor 1
```
- run the consumer
```
kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic first_topic
```
- run main method of application