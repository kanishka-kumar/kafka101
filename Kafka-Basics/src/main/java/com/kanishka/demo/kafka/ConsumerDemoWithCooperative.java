package com.kanishka.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoWithCooperative {

    private static final Logger log = LoggerFactory.getLogger(ConsumerDemoWithCooperative.class.getSimpleName());

    public static void main(String[] args) {
        log.info("ConsumerDemoWithCooperative Started");

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
        // By Default - partition.assignment.strategy = [class org.apache.kafka.clients.consumer.RangeAssignor, class org.apache.kafka.clients.consumer.CooperativeStickyAssignor]
        properties.setProperty("partition.assignment.strategy", CooperativeStickyAssignor.class.getName());// This set the partition assignment strategy.
        //strategy for static assignments, use group.instance.id. (By Default = null)
        //properties.setProperty("group.instance.id", "....");
        //Create a consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(properties);
        //Getting reference to main thread
        final Thread mainThread = Thread.currentThread();

        //Adding the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                log.info("Detected Shutdown, let's exit by calling consumer.wakeup()");
                consumer.wakeup();
                //Join the main thread to allow execution of code in main thread.
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //Subscribe to a topic
        consumer.subscribe(Arrays.asList(topic));

        try{
            while(true){
                log.info("Consuming from topic: " + topic);
                ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(1000));
                for(ConsumerRecord<String,String> record : records){
                    log.info("Key: " + record.key() + ", Value: " + record.value());
                    log.info("Partition: " + record.partition() + ", Offset: " + record.offset());
                }
            }
        } catch (WakeupException e){
            log.info("Consumer is Shutting down. "+e);
        } catch (Exception e){
            log.error("Unexpected exception occured in the consumer. ",e);
        } finally{
            //To close the consumer and commit the offsets.
            consumer.close();
            log.info("Consumer is now gracefully Shutdown.");
        }


    }
}
