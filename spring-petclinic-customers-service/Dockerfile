# Giai đoạn build
FROM maven:3.8.6-eclipse-temurin-17 AS builder

# Đặt thư mục làm việc
WORKDIR /app

# Copy toàn bộ source code để đảm bảo Maven có thể resolve parent POM
COPY .. .

# Build service cụ thể, ví dụ: spring-petclinic-customers-service
RUN mvn clean package -pl spring-petclinic-customers-service -am -DskipTests

# Giai đoạn runtime
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy file jar đã build vào image
COPY --from=builder /app/spring-petclinic-customers-service/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
