FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q -B dependency:go-offline

COPY src ./src
RUN ./mvnw -q -B -DskipTests package


FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN addgroup -S springgrp && adduser -S spring -G springgrp \
    && mkdir -p /app/logs && chown spring:springgrp /app/logs

USER spring:springgrp

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
