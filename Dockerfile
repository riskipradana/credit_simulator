# Use lightweight Java runtime
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy built jar
COPY target/credit-simulator.jar app.jar

# Run application
ENTRYPOINT ["java","-jar","app.jar"]