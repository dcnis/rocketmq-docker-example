# rocketmq-docker-example
A Quick start for how to use Apache RocketMQ with Docker

This project should show the high availability of RocketMQ. That is why we are using:
- 2x RocketMQ Nameserver
- 2x RocketMQ Master Broker
- 2x RocketMQ Slave Broker
- 2x microservices
  - notification-service (acts as RocketMQ Consumer)
  - order-service (acts as RocketMQ Producer)

This will be set up using docker-compose.yml

## 1. Setting up RocketMQ Nameserver
In our docker-compose.yml we are setting up our two RocketMQ Nameserver ```rocketmq-nameserver-a``` and ```rocketmq-nameserver-b```

```
version: "3.8"
services:
  rocketmq-nameserver-a:
    image: foxiswho/rocketmq:4.8.0
    container_name: rocketmq-nameserver-a
    ports:
      - "9876:9876"
    volumes:
      - ./data/nameserver-a/logs:/home/rocketmq/logs
    command: mqnamesrv
    networks:
      - rmq

  rocketmq-nameserver-b:
    image: foxiswho/rocketmq:4.8.0
    container_name: rocketmq-nameserver-b
    ports:
      - "9877:9876"
    volumes:
      - ./data/nameserver-b/logs:/home/rocketmq/logs
    command: mqnamesrv
    networks:
      - rmq
      
networks:
  rmq:
    name: rmq
    driver: bridge
```

## 2. Setting up RocketMQ Master Broker
We are setting up 2x Master broker. ````rocketmq-broker-a```` and ````rocketmq-broker-b````
```
  rocketmq-broker-a:
    image: foxiswho/rocketmq:4.8.0
    container_name: rocketmq-broker-a
    restart: always
    volumes:
      - ./data/broker-a/logs:/opt/logs
      - ./data/broker-a/store:/opt/store
      - ./data/broker-a/conf/broker.conf:/etc/rocketmq/broker.conf
    ports:
      - "10910:10910"
    environment:
      NAMESRV_ADDR: "rocketmq-nameserver-a:9876;rocketmq-nameserver-b:9876"
      JAVA_OPT_EXT: "-server -Xms128m -Xmx128m -Xmn128m"
    command: sh mqbroker -c /etc/rocketmq/broker.conf
    depends_on:
      - rocketmq-nameserver-a
    networks:
      - rmq
      

  rocketmq-broker-b:
    image: foxiswho/rocketmq:4.8.0
    container_name: rocketmq-broker-b
    restart: always
    volumes:
      - ./data/broker-b/logs:/opt/logs
      - ./data/broker-b/store:/opt/store
      - ./data/broker-b/conf/broker.conf:/etc/rocketmq/broker.conf
    ports:
      - "10920:10920"
    environment:
      NAMESRV_ADDR: "rocketmq-nameserver-a:9876;rocketmq-nameserver-b:9876"
      JAVA_OPT_EXT: "-server -Xms128m -Xmx128m -Xmn128m"
    command: mqbroker -c /etc/rocketmq/broker.conf
    depends_on:
      - rocketmq-nameserver-a
    networks:
      - rmq
```
The store folder where the brokers store their messages is internally stored in ```/opt/store```<br>
This can be configured using our ```./data/broker-a/conf/broker.conf```

```
storePathRootDir=/opt/store
storePathCommitLog=/opt/store/commitlog
storePathConsumerQueue=/opt/store/consumequeue
storePathIndex=/opt/store/index
storeCheckpoint=/opt/store/checkpoint
```

Because we are running everything on one single machine via docker, we cannot use a lot of heap space.
That's why it is very important that you don't use more than ```JAVA_OPT_EXT: "-server -Xms128m -Xmx128m -Xmn128m"```.
Otherwise you will likely encounter following problem:
```
/home/rocketmq/rocketmq-4.7.0/bin/runbroker.sh: line 158: 28 Killed $JAVA ${JAVA_OPT} $@
```

## 3. Setting up RocketMQ Slave Broker

```
  rocketmq-broker-a-s:
    image: foxiswho/rocketmq:4.8.0
    container_name: rocketmq-broker-a-s
    restart: always
    volumes:
      - ./data/broker-a-s/logs:/opt/logs
      - ./data/broker-a-s/store:/opt/store
      - ./data/broker-a-s/conf/broker.conf:/etc/rocketmq/broker.conf
    ports:
      - "10911:10911"
    environment:
      NAMESRV_ADDR: "rocketmq-nameserver-a:9876;rocketmq-nameserver-b:9876"
      JAVA_OPT_EXT: "-server -Xms128m -Xmx128m -Xmn128m"
    command: mqbroker -c /etc/rocketmq/broker.conf
    depends_on:
      - rocketmq-nameserver-a
    networks:
      - rmq
   
   
   
    rocketmq-broker-b-s:
    image: foxiswho/rocketmq:4.8.0
    container_name: rocketmq-broker-b-s
    restart: always
    volumes:
      - ./data/broker-b-s/logs:/opt/logs
      - ./data/broker-b-s/store:/opt/store
      - ./data/broker-b-s/conf/broker.conf:/etc/rocketmq/broker.conf
    ports:
      - "10921:10921"
    environment:
      NAMESRV_ADDR: "rocketmq-nameserver-a:9876;rocketmq-nameserver-b:9876"
      JAVA_OPT_EXT: "-server -Xms128m -Xmx128m -Xmn128m"
    command: mqbroker -c /etc/rocketmq/broker.conf
    depends_on:
      - rocketmq-nameserver-a
    networks:
      - rmq
```

## 4. Configuring Brokers with broker.conf

In our folder structure we are having a broker.conf for each broker

```
project-root
  /data
    /broker-a
      /conf
        broker.conf
    /broker-a-s
      /conf
        broker.conf
    /broker-b
      /conf
        broker.conf
    /broker-b-s
      /conf
        broker.conf
```

```broker-a``` `s broker.conf
```
brokerClusterName=DefaultCluster
brokerName=broker-a
brokerId=0
namesrvAddr=rocketmq-nameserver-a:9876;rocketmq-nameserver-b:9876
deleteWhen=04
fileReservedTime=48
autoCreateTopicEnable=true
autoCreateSubscriptionGroup=true
listenPort=10910
storePathRootDir=/opt/store
storePathCommitLog=/opt/store/commitlog
storePathConsumerQueue=/opt/store/consumequeue
storePathIndex=/opt/store/index
storeCheckpoint=/opt/store/checkpoint
brokerRole=SYNC_MASTER
flushDiskType=ASYNC_FLUSH
```

```broker-a-s``` `s broker.conf
```
brokerClusterName=DefaultCluster
brokerName=broker-a
brokerId=1
namesrvAddr=rocketmq-nameserver-a:9876;rocketmq-nameserver-b:9876
deleteWhen=04
fileReservedTime=48
autoCreateTopicEnable=true
autoCreateSubscriptionGroup=true
listenPort=10911
storePathRootDir=/opt/store
storePathCommitLog=/opt/store/commitlog
storePathConsumerQueue=/opt/store/consumequeue
storePathIndex=/opt/store/index
storeCheckpoint=/opt/store/checkpoint
abortFile=/opt/store/abort
brokerRole=SLAVE
flushDiskType=ASYNC_FLUSH
```


```broker-b``` `s broker.conf
```
brokerClusterName=DefaultCluster
brokerName=broker-b
brokerId=0
namesrvAddr=rocketmq-nameserver-a:9876;rocketmq-nameserver-b:9876
deleteWhen=04
fileReservedTime=48
autoCreateTopicEnable=true
autoCreateSubscriptionGroup=true
listenPort=10920
storePathRootDir=/opt/store
storePathCommitLog=/opt/store/commitlog
storePathConsumerQueue=/opt/store/consumequeue
storePathIndex=/opt/store/index
storeCheckpoint=/opt/store/checkpoint
brokerRole=SYNC_MASTER
flushDiskType=ASYNC_FLUSH
```

```broker-b-s``` `s broker.conf
```
brokerClusterName=DefaultCluster
brokerName=broker-b
brokerId=1
namesrvAddr=rocketmq-nameserver-a:9876;rocketmq-nameserver-b:9876
autoCreateTopicEnable=true
autoCreateSubscriptionGroup=true
deleteWhen=04
fileReservedTime=48
listenPort=10921
storePathRootDir=/opt/store
storePathCommitLog=/opt/store/commitlog
storePathConsumerQueue=/opt/store/consumequeue
storePathIndex=/opt/store/index
storeCheckpoint=/opt/store/checkpoint
brokerRole=SLAVE
flushDiskType=ASYNC_FLUSH
```

Note: The Slave broker has the same ```brokerName``` as the Master Broker but Master Broker has ```brokderId```=0
and Slave Broker has ```brokerId```=1.

Slave Broker has ```brokerRole```=SLAVE while Master Broker has ```brokerRole```=SYNC_MASTER

```flushDiskType```=ASYNC_FLUSH makes sure we have high performance and throughput but if there is a
disc crash there might be a small data loss. If you cannot tolerate small message loss you can consider using
```flushDiskType```=SYNC_FLUSH which dramatically decreases performance and throughput though because of the 
roundtrips to the disc.

## 5. Setting up rocketmq-dashboard

[rocketmq-dashboard](https://github.com/apache/rocketmq-dashboard) provides excellent Apache RocketMQ monitoring capabilities.

```
  rocketmq-dashboard:
    image: apacherocketmq/rocketmq-dashboard:1.0.0
    container_name: rocketmq-dashboard
    ports:
      - "8080:8080"
    depends_on:
      - rocketmq-nameserver-a
      - rocketmq-nameserver-b
    environment:
      NAMESRV_ADDR: "rocketmq-nameserver-a:9876;rocketmq-nameserver-b:9876"
      JAVA_OPTS_EXT: "-server -Xms128m -Xmx128m -Xmn128m -Drocketmq.namesrv.addr=rocketmq-nameserver-a:9876;rocketmq-nameserver-b:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false"
    networks:
      - rmq
```

## 6. Setting up microservices
```notification-service``` is a Spring Boot Application and acts as RocketMQ Consumer.
```
  notification-service:
    container_name: notification-service
    build:
      dockerfile: ./docker/notification-service.Dockerfile
    depends_on:
      - rocketmq-nameserver-a
      - rocketmq-nameserver-b
    ports:
      - "8081:8081"
    volumes:
      - ./logs:/home/logs
    networks:
      - rmq
```

```order-service``` is a Spring Boot Application and acts as RocketMQ Producer.
```
  order-service:
    container_name: order-service
    build:
      dockerfile: ./docker/order-service.Dockerfile
    depends_on:
      - rocketmq-nameserver-a
      - rocketmq-nameserver-b
    ports:
      - "8083:8083"
    volumes:
      - ./logs:/home/logs
    networks:
      - rmq
```







