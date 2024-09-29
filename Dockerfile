#build the app
FROM maven:3.8.1-openjdk-17 AS BUILD
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

#create final image
FROM openjdk:17.0.1-slim
WORKDIR /app
COPY --from=BUILD /app/target/sampling-server-0.0.1-SNAPSHOT.jar server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "server.jar"]
