# Stage 1: Build Maven project
FROM maven:3.8.6-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy toàn bộ source code (gồm nhiều module)
COPY . .

# Chuyển vào thư mục của service chính (ví dụ: api-gateway)
WORKDIR /build/${SERVICE}

# Build service chính
RUN mvn clean package -DskipTests

# Stage 2: Tạo image chạy nhẹ
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy file JAR đã build từ stage 1
COPY --from=builder /build/${SERVICE}/target/*.jar app.jar

# Mở port service chính (nếu là gateway thường là 8080 hoặc 8081)
EXPOSE 8080

# Chạy ứng dụng
CMD ["java", "-jar", "app.jar"]
