# 1. Этап сборки (используем стабильный Liberica JDK из открытого реестра BellSoft)
FROM bellsoft/liberica-openjdk-alpine:17 AS build
WORKDIR /build
# Копируем проект и скрипт обертки Maven (mvnw), который идет вместе со Spring
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
# Даём права на выполнение (важно для Windows)
RUN chmod +x mvnw
# Запускаем сборку через локальный mvnw (он сам докачает нужный Maven)
RUN ./mvnw clean package -DskipTests

# 2. Этап запуска (используем тот же образ, но можно и JRE-версию)
FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
