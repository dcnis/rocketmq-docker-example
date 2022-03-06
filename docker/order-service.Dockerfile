FROM maven:3.8.3-jdk-11 as build
WORKDIR /build

# copy parent-pom
COPY ./pom.xml .
# copy child pom.xmls
COPY ./shared/pom.xml /build/shared/
COPY ./order-service/pom.xml /build/order-service/

RUN mvn package -Porder-service -DskipTests

# copy child module sources
COPY ./shared/src /build/shared/src
COPY ./order-service/src /build/order-service/src

RUN mvn -o package -Porder-service -DskipTests

FROM openjdk:11-jre-slim AS runtime
WORKDIR /app
EXPOSE 8083
COPY --from=build /build/order-service/target .
CMD ["java", "-jar", "./order-service-0.0.1-SNAPSHOT.jar"]

