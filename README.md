# Messaging System implemented with Kafka

This project contains a producer and a consumer implementation connected to a Kafka Broker.

The basic package contains a producer, sending a record to the Kafka Broker, and consumer, receiving that record as an implementation by example.

The twitter package contains: 
### TwitterProducer
- Obtains data from Tweets on Twitter containing a special term
- Sends the filtered Tweets to a Kafka topic

### TwitterConsumer
- Consumes tweets from that topic
- Deserializes tweets
- Saves tweets id, creation date, consumed date and source in a database 

## Prerequisites:
- Apache Kafka installation
- Twitter developer credentials
- Sqlite installation 
- Database named "twittertweets.db" with table "tweets" having columns: id_str, created_at, consumed_at, consumed_through

## Run the application:

- Start the zookeeper
```
    zookeeper-servestart.sh config/zookeeper.properties
```
- Start kafka server
```
start kakfa: kafka-server-start.sh config/server.properties
```
- Create the topic
```
kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic first_topic --create --partitions 3 --replication-factor 1
```

- Run main method of Main.java