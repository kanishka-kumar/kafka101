package com.kanishka.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConsumerDemo {
    private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class.getSimpleName());

    public static void main(String[] args) {
        log.info("Started Kafka Consumer.");

        String groupId = "my-java-application";
        String topic = "demo_java_topic";
        //Create Consumer Properties
        Properties properties = new Properties();
        //Connect to localhost
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");

        //Create Consumer configs
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());

        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");// earliest = Reading from beginning of the topic

        //Create a consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(properties);

        //Subscribe to a topic
        consumer.subscribe(List.of(topic));

        //Poll for data
        while(true){
            log.info("Consuming from topic: " + topic);
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(1000));// Consumer would wait for max 1000 milliSec

            for(ConsumerRecord<String,String> record:records){
                log.info("Key: "+record.key()+",\t Value: "+record.value());
                log.info("Partition: "+record.partition()+",\t Offset: "+record.offset());
            }
        }
    }
}
