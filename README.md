# kafka101
Creating Kafka Based Application

-- Starting Kafka in local 
--

* Modify config/server.properties as below and save it.
    + Edit listeners ~ listeners=PLAINTEXT://localhost:9092
* Start Zookeeper and Kafka Server
    + zookeeper-server-start.sh ~/kafka_2.13-3.9.1/config/zookeeper.properties
    + kafka-server-start.sh ~/kafka_2.13-3.9.1/config/server.properties

##### Kafka Commands

###### List of Kafka Topic
```shell
kafka-topics.sh --bootstrap-server localhost:9092 --list
```
###### Creating Kafka Topic
```shell
kafka-topics.sh --bootstrap-server localhost:9092 --topic topic_name --create
```
###### Creating Kafka Topic with Partition
```shell
kafka-topics.sh --bootstrap-server localhost:9092 --topic topic_name --create --partition number_of_partitions
```
###### Creating Kafka Topic with Partition along with Replication Factor
```shell
kafka-topics.sh --bootstrap-server localhost:9092 --topic topic_name --create --partition number_of_partitions --replication-factor number_of_replication
```
###### To consume the message that was produced using kafka-console-consumer

```shell
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic_name --from-beginning
```