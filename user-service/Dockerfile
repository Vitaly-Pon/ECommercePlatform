FROM bellsoft/liberica-openjdk-alpine:17 AS build
WORKDIR /build

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

# User-service port
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
