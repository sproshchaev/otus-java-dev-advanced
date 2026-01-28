plugins {
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("java")
}

group = "ru.otus"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    // Добавляем репозиторий для Apache Camel
    maven { url = uri("https://repo.spring.io/libs-milestone") }
    maven { url = uri("https://repo.spring.io/libs-release") }
}

extra["camel.version"] = "4.14.0"
extra["otel.version"] = "1.54.0"

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Apache Camel Starters
    implementation("org.apache.camel.springboot:camel-spring-boot-starter:${project.extra["camel.version"]}")
    implementation("org.apache.camel.springboot:camel-yaml-dsl-starter:${project.extra["camel.version"]}")
    implementation("org.apache.camel.springboot:camel-resilience4j-starter:${project.extra["camel.version"]}")
    implementation("org.apache.camel.springboot:camel-jackson-starter:${project.extra["camel.version"]}")
    implementation("org.apache.camel.springboot:camel-jq-starter:${project.extra["camel.version"]}")
    implementation("org.apache.camel.springboot:camel-http-starter:${project.extra["camel.version"]}")
    implementation("org.apache.camel.springboot:camel-console-starter:${project.extra["camel.version"]}")
    implementation("org.apache.camel.springboot:camel-management-starter:${project.extra["camel.version"]}")
    implementation("org.apache.camel.springboot:camel-spring-rabbitmq-starter:${project.extra["camel.version"]}")
    implementation("org.apache.camel.springboot:camel-micrometer-starter:${project.extra["camel.version"]}")
    implementation("org.apache.camel.springboot:camel-disruptor-starter:${project.extra["camel.version"]}")

    // Добавляем Kafka компонент для Camel
    implementation("org.apache.camel.springboot:camel-kafka-starter:${project.extra["camel.version"]}")

    // Apache Camel Core Dependencies (without starter)
    implementation("org.apache.camel:camel-jsonpath:${project.extra["camel.version"]}")

    // Micrometer for Metrics
    implementation("io.micrometer:micrometer-registry-prometheus")

    // OpenTelemetry for Distributed Tracing
    implementation("io.opentelemetry:opentelemetry-api:${project.extra["otel.version"]}")
    implementation("io.opentelemetry:opentelemetry-sdk:${project.extra["otel.version"]}")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp:${project.extra["otel.version"]}")

    // Testing Dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.apache.camel:camel-test-spring-junit5:${project.extra["camel.version"]}")
    testImplementation("org.awaitility:awaitility:4.2.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}