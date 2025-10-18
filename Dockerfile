# Stage 1: Build the application with Maven
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app

# IMPORTANT: Copy the contents of the 'project' subfolder, not the root
COPY project/ .

RUN mvn clean package -DskipTests

# Stage 2: Create a minimal runtime image
FROM openjdk:17-slim
WORKDIR /app

# This line is correct because the build context is now flat
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]