FROM maven:3.8.6-eclipse-temurin-17 AS builder

WORKDIR /build
COPY . .

# Build toàn bộ các module
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy .jar đúng theo service cần dùng
ARG SERVICE
COPY --from=builder /build/${SERVICE}/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
