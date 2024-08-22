FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

#FROM openjdk:17-jdk-slim
#WORKDIR /app
#COPY target/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY .env .env
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
