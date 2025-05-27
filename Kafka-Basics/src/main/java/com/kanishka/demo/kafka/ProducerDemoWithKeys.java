package com.kanishka.demo.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithKeys {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoWithKeys.class.getSimpleName());

    public static void main(String[] args) {
        log.info("Kafka ProducerDemoWithKeys started.");

        //Create Producer Properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");

        //Connecting to Kafka Secure Cluster
        /*properties.setProperty("bootstrap.servers", "cluster.kafka.xyz:9092");
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.mechanism", "PLAIN");
        properties.setProperty("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username="<your username>" password="<your password>"");*/

        //Set Producer Properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());


        properties.setProperty("batch.size", "400");

        //To explicitly use Round Robin to message go to every partition. But, not recommended.
        //properties.setProperty("partitioner.class", RoundRobinPartitioner.class.getSimpleName());

        //Create the Producer
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        for(int i=0;i<2;i++){
            for(int j=0;j<10;j++){
                String topic = "demo_java_topic";
                String key = "id_"+j;
                String value = "Message: Hello World -> "+j;

                ProducerRecord<String, String> producerRecord =
                        new ProducerRecord<>(topic, key, value);

                producer.send(producerRecord, (metadata, e) -> {
                    if(e == null){
                        log.info("Key: "+key +" | Partition: "+metadata.partition());
                    } else {
                        log.error("Error while producing: "+e);
                    }
                });
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //Flush and Close the Producer
        producer.close();// it will also call flush() method, so no need to explicitly call flush().


    }
}
