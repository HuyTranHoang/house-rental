#FROM openjdk:17-jdk-slim
#WORKDIR /app
#COPY target/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

#FROM openjdk:17-jdk-slim
#WORKDIR /app
#EXPOSE 8080
#COPY .env .env
#COPY target/*.jar app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]