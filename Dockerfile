# Giai đoạn build
FROM maven:3.8.6-eclipse-temurin-17 AS builder

WORKDIR /build

COPY . .

ARG SERVICE
WORKDIR /build/${SERVICE}
RUN mvn clean package -DskipTests

# Giai đoạn runtime
FROM eclipse-temurin:17-jdk-jammy

ARG SERVICE
WORKDIR /app
COPY --from=builder /build/${SERVICE}/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
