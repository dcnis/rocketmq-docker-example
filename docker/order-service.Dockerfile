FROM maven:3.8.3-jdk-11
WORKDIR /app

# copy parent-pom
COPY ./pom.xml .
# copy child pom.xmls
COPY ./shared/pom.xml /app/shared/
COPY ./order-service/pom.xml /app/order-service/

RUN mvn package -Porder-service

# copy child pom.xmls
COPY ./shared/pom.xml /app/shared/
COPY ./order-service/pom.xml /app/order-service/

RUN mvn -o package -Porder-service

EXPOSE 8080

CMD ["java", "-jar", "./order-service/target/order-service-0.0.1-SNAPSHOT.jar"]
