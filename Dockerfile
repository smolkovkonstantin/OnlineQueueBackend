# Используем базовый образ с JDK для сборки приложения
FROM maven:3.8.1-openjdk-17 AS builder

# Копируем файлы проекта внутрь образа
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Выполняем сборку приложения
RUN mvn clean install

# Используем образ с JRE для запуска приложения
FROM openjdk:17-jdk-slim

# Создаем директорию приложения внутри образа
WORKDIR /app

# Копируем JAR файл приложения из предыдущего этапа сборки
COPY --from=builder /app/target/backend_java-0.0.1.jar /app/backend_java-0.0.1.jar

# Копируем файл application.yml внутрь контейнера
COPY src/main/resources/application-dev.yml /app/application-dev.yml

# Указываем при запуске JVM путь к файлу application.yml
CMD ["java", "-jar", "-Dspring.config.location=/app/application-dev.yml", "backend_java-0.0.1.jar"]