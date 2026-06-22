# hello-service — Контейнеризация Java-приложений с Docker

Демо-проект к открытому уроку курса **«Java разработчик. Экспертный уровень»** (OTUS).

Маленький сервис на **Spring Boot 3.5** с одним REST-контроллером. БД нет.
Цель — показать на практике основные приёмы контейнеризации из презентации:
Dockerfile → сборка образа → запуск контейнера → управление контейнерами.

## Стек

- Java 21 (toolchain), Gradle 8.14
- Spring Boot 3.5 (`spring-boot-starter-web`)
- Docker (базовый образ `eclipse-temurin:21`)

## Структура проекта

```
src/main/java/ru/otus/
  ├── HelloApplication.java   # точка входа Spring Boot
  └── HelloController.java     # REST-контроллер: GET / и GET /health
Dockerfile                     # простой образ — как на слайдах (COPY готового jar)
Dockerfile.multistage          # бонус: multi-stage build (jar собирается внутри образа)
.dockerignore
build.gradle.kts
```

## Эндпоинты

| Метод | Путь        | Описание                                                |
|-------|-------------|---------------------------------------------------------|
| GET   | `/`         | Приветствие + имя хоста (в контейнере это его ID)        |
| GET   | `/?name=X`  | Приветствие с именем `X`                                 |
| GET   | `/health`   | Проверка живости: `{"status":"UP"}`                     |

---

## 1. Запуск без Docker (локально)

> ⚠️ Gradle 8.14 не запускается на Java 25. Сборку нужно выполнять на **JDK 21**.
> Если в системе по умолчанию JDK 25, укажите JAVA_HOME явно:
> `export JAVA_HOME=$(/usr/libexec/java_home -v 21)`

```bash
# собрать исполняемый jar
./gradlew bootJar

# прогнать тесты
./gradlew test

# запустить приложение
./gradlew bootRun
# либо напрямую из jar:
java -jar build/libs/otus-java-dev-advanced-1.0-SNAPSHOT.jar
```

Проверка:
```bash
curl http://localhost:8080/
curl http://localhost:8080/health
```

---

## 2. Сборка Docker-образа

### Вариант А — простой Dockerfile (как на слайдах)

Сначала собираем jar, потом кладём его в образ:

```bash
./gradlew bootJar
docker build -t hello-service:1.0 .
```

### Вариант Б — multi-stage (jar собирается внутри образа)

Локальная сборка не нужна — Docker сам соберёт проект:

```bash
docker build -f Dockerfile.multistage -t hello-service:1.0 .
```

### Современный способ — buildx (мультиплатформенная сборка)

`docker build` собирает образ только под текущую архитектуру и помечен как legacy
с версии v23.0. Для нескольких платформ используют `buildx`:

```bash
docker buildx create --name my-builder      # создать билдер
docker buildx use my-builder                # сделать активным
docker buildx build --platform linux/amd64,linux/arm64 -t hello-service:1.0 .
```

Посмотреть список образов:
```bash
docker images
```

---

## 3. Запуск контейнера из образа

```bash
# -d  — запуск в фоне (detached), выводит ID контейнера
# -p  — маппинг порта host:container (8080 снаружи -> 8080 внутри)
docker run -d --name hello -p 8080:8080 hello-service:1.0

# список запущенных контейнеров
docker ps
```

Проверка работы:
```bash
curl http://localhost:8080/
# {"message":"Привет, OTUS! ...","host":"<ID контейнера>"}
```

Поле `host` равно ID контейнера — наглядная иллюстрация изоляции окружения
(«окружение в окружении» из презентации).

---

## 4. Управление контейнерами

Общий вид команды: `docker <команда> <ID|имя контейнера>`

| Команда                    | Действие                                  |
|----------------------------|-------------------------------------------|
| `docker stop hello`        | остановить контейнер                       |
| `docker start hello`       | запустить контейнер                        |
| `docker restart hello`     | перезапустить                              |
| `docker pause hello`       | приостановить на время                     |
| `docker unpause hello`     | возобновить                                |
| `docker kill hello`        | принудительно остановить                   |
| `docker rm hello`          | удалить контейнер (предварительно stop)    |
| `docker rm -f hello`       | удалить даже работающий контейнер          |

Полезные команды наблюдения:

```bash
docker logs hello        # вывод stdout приложения
docker logs -f hello     # следить за логами в реальном времени
docker stats             # потребление ресурсов всеми контейнерами
docker attach hello      # подключить потоки stdin/stdout/stderr
docker commit hello my-snapshot:1.0   # сохранить состояние контейнера в образ
```

---

## 5. Полный сценарий для демонстрации на уроке

```bash
# 1. собрать jar
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
./gradlew bootJar

# 2. собрать образ
docker build -t hello-service:1.0 .

# 3. запустить контейнер
docker run -d --name hello -p 8080:8080 hello-service:1.0

# 4. проверить
docker ps
curl http://localhost:8080/
docker logs hello

# 5. управление
docker stop hello
docker start hello

# 6. очистка
docker rm -f hello
docker rmi hello-service:1.0
```

---

## Соответствие слайдам презентации

| Тема слайда                         | Где в проекте                         |
|-------------------------------------|---------------------------------------|
| Dockerfile (FROM/RUN/COPY/EXPOSE/CMD) | `Dockerfile`                        |
| Сборка образа `docker build -t`     | раздел 2                              |
| Современный способ — `buildx`       | раздел 2                              |
| Запуск `docker run -d`, `docker ps` | раздел 3                              |
| Управление контейнерами             | раздел 4                              |
| GUI (Docker Desktop, Portainer)     | используется Docker Desktop           |
