# Простой Dockerfile — ровно как на слайдах урока.
# Перед сборкой образа нужно собрать jar: ./gradlew bootJar

# 1) Берём готовый образ с JRE из DockerHub (родительский образ)
FROM eclipse-temurin:21-jre

# 2) Создаём директорию внутри образа и копируем в неё наше приложение
RUN mkdir /opt/app
COPY build/libs/otus-java-dev-advanced-1.0-SNAPSHOT.jar /opt/app/app.jar

# 3) Объявляем порт, на котором слушает приложение
EXPOSE 8080

# 4) Команда, которая выполнится при старте контейнера
CMD ["java", "-jar", "/opt/app/app.jar"]
