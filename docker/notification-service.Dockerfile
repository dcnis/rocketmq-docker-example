FROM maven:3.8.3-jdk-11 as maven_build
WORKDIR /app

# copy parent-pom
COPY ./pom.xml .
# copy child pom.xmls
COPY ./shared/pom.xml /app/shared/
COPY ./notification-service/pom.xml /app/notification-service/

RUN mvn package -Pnotification-service

# copy child module sources
COPY ./shared/src /app/shared/src
COPY ./notification-service/src /app/notification-service/src

RUN mvn -o package -Pnotification-service

EXPOSE 8080

CMD ["java", "-jar", "./notification-service/target/notification-service-0.0.1-SNAPSHOT.jar"]
