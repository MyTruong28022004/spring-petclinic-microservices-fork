# Giai đoạn build
FROM maven:3.8.6-eclipse-temurin-17 AS builder

ARG SERVICE
WORKDIR /build

# Copy toàn bộ source code để đảm bảo parent POM tồn tại
COPY . .

# Build service (bỏ qua test để nhanh hơn)
RUN cd ${SERVICE} && mvn clean package -DskipTests

# Giai đoạn runtime
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy file JAR từ giai đoạn build
COPY --from=builder /build/${SERVICE}/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
