FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q -B dependency:go-offline

COPY src ./src
RUN ./mvnw -q -B -DskipTests package


FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN addgroup -S springGrp && adduser -S spring -G springGrp \
    && mkdir -p /app/logs && chown spring:springGrp /app/logs

USER spring:springGrp

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]