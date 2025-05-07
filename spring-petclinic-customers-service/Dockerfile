# Giai đoạn build
FROM maven:3.8.6-eclipse-temurin-17 AS builder

WORKDIR /build

COPY . .

WORKDIR /build/spring-petclinic-api-gateway

RUN mvn clean package -DskipTests

# Giai đoạn runtime
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=builder /build/spring-petclinic-customers-service/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
