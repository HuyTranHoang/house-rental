#FROM openjdk:17-jdk-slim
#WORKDIR /app
#COPY target/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM openjdk:17-jdk-slim
WORKDIR /app
EXPOSE 8080
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
