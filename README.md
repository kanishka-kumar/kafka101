# kafka101
Creating Kafka Based Application

Starting Kafka in local 
--

* Modify config/server.properties as below and save it.
    + Edit listeners -- listeners=PLAINTEXT://localhost:9092

## Start Zookeeper and Kafka Server using binaries in WSL2

```Shell
    zookeeper-server-start.sh ~/kafka_2.13-3.9.1/config/zookeeper.properties
    kafka-server-start.sh ~/kafka_2.13-3.9.1/config/server.properties
```
## If we encounter error - [producer clientid=producer-1] connection to node -1 (/127.0.0.1:9092) could not be established. 

#### Then stop Kafka and Zookeeper. And execute below command. After this, please relaunch Zookeeper and Kafka
  ```shell
  sudo sysctl -w net.ipv6.conf.all.disable_ipv6=1
  sudo sysctl -w net.ipv6.conf.default.disable_ipv6=1
  ```

## Kafka Commands

###### List of Kafka Topic
```shell
kafka-topics.sh --bootstrap-server localhost:9092 --list
```
###### Creating Kafka Topic
```shell
kafka-topics.sh --bootstrap-server localhost:9092 --topic topic_name --create
```
###### Creating Kafka Topic with Partitions
```shell
kafka-topics.sh --bootstrap-server localhost:9092 --topic topic_name --create --partitions number_of_partitions
```
###### Creating Kafka Topic with Partitions along with Replication Factor
```shell
kafka-topics.sh --bootstrap-server localhost:9092 --topic topic_name --create --partitions number_of_partitions --replication-factor number_of_replication
```
###### To delete an existing topic
```shell
kafka-topics.sh --bootstrap-server localhost:9092 --topic topic_name --delete
```

###### To consume the message that was produced using kafka-console-consumer

```shell
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic_name --from-beginning
```