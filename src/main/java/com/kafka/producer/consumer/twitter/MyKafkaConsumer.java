package com.kafka.producer.consumer.twitter;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

public class MyKafkaConsumer implements Runnable {

    private int id;

    public MyKafkaConsumer(int id) {
        this.id = id;
    }

    private DatabaseConnector databaseConnector = new DatabaseConnector();

    public void run() {
        final Logger logger = LoggerFactory.getLogger(MyKafkaConsumer.class);
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");

        //create the consumer
        KafkaConsumer consumer = new KafkaConsumer(getProperties());
        //subscribe to topic
        consumer.subscribe(Arrays.asList("twitter_tweets"));

        //poll new data
        while (true) {
            ConsumerRecords<String, String> records =  consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                Tweet tweet = null;
                try {
                    tweet = mapper.readValue(record.value(), Tweet.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Date now = new Date();
                tweet.setConsumed_at(dateFormat.format(now));

                logger.info("Consumer " + id + " " + record.value());
                writeTweetIntoDb(tweet);
            }
        }
    }

    private void writeTweetIntoDb(Tweet tweet){
        databaseConnector.insert(tweet.getId(), tweet.getCreated_at(), tweet.getConsumed_at(), "Kafka");
    }

    private Properties getProperties(){
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "groupId" + id);
        //read from the beginning of the topic
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }
}
