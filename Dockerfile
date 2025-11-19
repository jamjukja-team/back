FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["sh", "-c", "sleep 10 && java -jar app.jar"]