FROM openjdk:8-jdk-alpine

EXPOSE 8080

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} spring-cache-redis.jar

ENTRYPOINT ["java","-jar","/spring-cache-redis.jar"]