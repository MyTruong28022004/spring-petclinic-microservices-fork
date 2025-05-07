# Sử dụng image Java 17 slim
FROM openjdk:17-jdk-slim

# Tạo một thư mục ứng dụng
WORKDIR /app

# Sao chép file .jar vào container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Mở cổng ứng dụng (theo cấu hình mặc định trong Spring Boot)
EXPOSE 8083

# Chạy ứng dụng khi container bắt đầu
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
