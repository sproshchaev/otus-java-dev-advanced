plugins {
    id("java")
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "ru.otus"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter")

    // Apache Camel Spring Boot Starter (основной)
    implementation("org.apache.camel.springboot:camel-spring-boot-starter:4.17.0")

    // Стартеры для компонентов RabbitMQ и Kafka
    implementation("org.apache.camel.springboot:camel-rabbitmq-starter:3.22.4")
    implementation("org.apache.camel.springboot:camel-kafka-starter:4.17.0")

    // Spring Boot тестирование
    // testImplementation("org.springframework.boot:spring-boot-starter-test")
    // testImplementation("org.apache.camel:camel-test-spring-junit5:3.22.3")
}

tasks.test {
    useJUnitPlatform()
}