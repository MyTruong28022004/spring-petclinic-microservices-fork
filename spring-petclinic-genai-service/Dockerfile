# Giai đoạn build
FROM maven:3.8.6-eclipse-temurin-17 AS builder

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

# Giai đoạn runtime
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
