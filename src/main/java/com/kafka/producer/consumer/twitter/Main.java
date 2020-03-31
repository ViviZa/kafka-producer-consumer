package com.kafka.producer.consumer.twitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class  Main {

    private static String secret = "";
    private static String consumerSecret = "";

    private static String consumerKey = "";
    private static String token = "";

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        MyKafkaProducer myKafkaProducer = new MyKafkaProducer(consumerKey, token, consumerSecret, secret);
        executorService.execute(myKafkaProducer);

        MyKafkaConsumer consumer = new MyKafkaConsumer(1);
        executorService.execute(consumer);
        //multiple consumer
        /*TwitterConsumer consumer2 = new TwitterConsumer(2);
        executorService.execute(consumer2);*/

        // multiple producer
        /*Producer producerWithSingleMessage = new Producer();
        executorService.execute(producerWithSingleMessage);*/
    }
}
