package com.kanishka.demo.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallback {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoWithCallback.class.getSimpleName());

    public static void main(String[] args) {
        log.info("Kafka ProducerDemoWithCallBack Started");

        //Create Producer Properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");//unsecure connection to localhost
        //Connecting to Kafka Secure Cluster
        /*properties.setProperty("bootstrap.servers", "cluster.kafka.xyz:9092");
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.mechanism", "PLAIN");
        properties.setProperty("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username="<your username>" password="<your password>"");*/

        //Set Producer Properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        //To explicitly use Round Robin to message go to every partition. But, not recommended.
        //properties.setProperty("partitioner.class", RoundRobinPartitioner.class.getSimpleName());

        //Create the Producer
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        for (int i = 0; i < 10; i++) {
            for(int j=0;j<30;j++){
                //Create a Producer Record, that is sent to Kafka
                ProducerRecord<String,String> producerRecord =
                        new ProducerRecord<>("demo_java_topic", i+" --> Hello World -> "+j);

                //Send Data Async
                producer.send(producerRecord, (recordMetadata, e) -> {
                    //Executes everu time a record is successfully sent or exception is thrown.
                    if(e == null) {
                        log.info("Received New Metadata \n" +
                                "Topic :"+ recordMetadata.topic() + "\n"+
                                "Partition: "+recordMetadata.partition()+ "\n"+
                                "Offset: "+recordMetadata.offset()+ "\n"+
                                "Timestamp: "+recordMetadata.timestamp());
                    } else
                        log.error("Error while Producing New Metadata", e);
                });
            }
        }


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Tells the producer to send all data and block until done -- synchronous
        producer.flush();

        //Flush and Close the Producer
        producer.close();// it will also call flush() method, so no need to explicitly call flush().

    }
}
