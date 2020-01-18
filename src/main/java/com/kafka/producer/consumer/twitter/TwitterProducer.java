package com.kafka.producer.consumer.twitter;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class TwitterProducer {

    private final String consumerKey = "vC4bsWbRLKyRIiuubqulY6oTZ";
    private final String consumerSecret = "SjsFzeMU6XAZZWgFS9MjxDqZXG2rhHgAPQY4w7Y3VKkhYH3evt";
    private final String token = "1218470536482893824-C0lBoZSTFs9KyuoeAv77XOolSkUkl1";
    private final String secret = "6RSx33UhjSrRd3i0n1IbZYsEvi5cimHuFivkblc86e6Zf";
    private Logger logger = LoggerFactory.getLogger(TwitterProducer.class);

    public TwitterProducer() {}

    public static void main(String[] args) {
        new TwitterProducer().processTweets();
    }

    public void processTweets() {
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(getKafkaProperties());

        //setup Twitter client
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(1000);
        Client client = createTwitterClient(msgQueue);
        client.connect();

        //ensure by application stop that all produced messages got send to kafka
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            client.stop();
            kafkaProducer.close();
        }));

        while (!client.isDone()) {
            String msg = null;
            try {
                msg = msgQueue.poll(5, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                client.stop();
            }
            if (msg != null) {
                logger.info(msg);
                ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("twitter_tweets", msg);
                kafkaProducer.send(producerRecord, new Callback() {
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (e != null) {
                            logger.error("An error happened");
                        }
                    }
                });
            }
        }
    }

    private Properties getKafkaProperties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    private Client createTwitterClient(BlockingQueue<String> msgQueue) {
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        List<String> terms = Lists.newArrayList("bitcoin");
        hosebirdEndpoint.trackTerms(terms);

        Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, secret);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        return builder.build();
    }
}
