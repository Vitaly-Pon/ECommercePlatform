FROM bellsoft/liberica-openjdk-alpine:21 AS build
WORKDIR /build

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]
