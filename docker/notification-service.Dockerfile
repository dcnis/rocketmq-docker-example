FROM maven:3.8.3-jdk-11 as build
WORKDIR /build

# copy parent-pom
COPY ./pom.xml .
# copy child pom.xmls
COPY ./shared/pom.xml /build/shared/
COPY ./notification-service/pom.xml /build/notification-service/

RUN mvn package -Pnotification-service

# copy child module sources
COPY ./shared/src /build/shared/src
COPY ./notification-service/src /build/notification-service/src

RUN mvn -o package -Pnotification-service

FROM openjdk:11-jre-slim AS runtime
WORKDIR /app
EXPOSE 8082
COPY --from=build /build/notification-service/target .
CMD ["java", "-jar", "./notification-service-0.0.1-SNAPSHOT.jar"]
