package com.kanishka.demo.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
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
        properties.setProperty("sasl.jaas.config","");*/

        //Set Producer Properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        //Create the Producer
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        for(int i=0;i<10;i++){
            //Create a Producer Record, that is sent to Kafka
            ProducerRecord<String,String> producerRecord =
                    new ProducerRecord<>("demo_java_topic", "Hello World-> "+i);

            //Send Data Async
            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    //Executes everu time a record is successfully sent or exception is thrown.
                    if(e == null) {
                        log.info("Received New Metadata \n" +
                                "Topic :"+ recordMetadata.topic() + "\n"+
                                "Partition: "+recordMetadata.partition()+ "\n"+
                                "Offset: "+recordMetadata.offset()+ "\n"+
                                "Timestamp: "+recordMetadata.timestamp());
                    } else
                        log.error("Error while Producing New Metadata", e);
                }
            });
        }

        //Tells the producer to send all data and block until done -- synchronous
        producer.flush();

        //Flush and Close the Producer
        producer.close();// it will also call flush() method, so no need to explicitly call flush().

    }
}
