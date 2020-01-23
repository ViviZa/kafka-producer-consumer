package com.kafka.producer.consumer.twitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static String secret = "";
    private static String consumerSecret = "";

    private static String consumerKey = "";
    private static String token = "";

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        TwitterProducer producer = new TwitterProducer(consumerKey, token, consumerSecret, secret);
        executorService.execute(producer);
        TwitterConsumer consumer = new TwitterConsumer();
        executorService.execute(consumer);
    }
}
