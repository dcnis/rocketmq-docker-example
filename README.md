# rocketmq-docker-example
A Quick start for how to use Apache RocketMQ with Docker






## Build docker image from Dockerfile for microservice order-service
```
docker build -f ./docker/order-service.Dockerfile -t order-service-image:0.0.1-SNAPSHOT .
```

## Build docker image from Dockerfile for microservice for notification-service
```
docker build -f ./docker/notification-service.Dockerfile -t notification-service-image:0.0.1-SNAPSHOT .
```
