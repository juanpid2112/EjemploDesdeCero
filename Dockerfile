# --- Etapa 1: compilación (Maven + JDK) ---
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline

COPY src ./src
RUN ./mvnw -q -DskipTests package

# --- Etapa 2: runtime (solo JRE + JAR) ---
FROM eclipse-temurin:21-jre-alpine

LABEL org.opencontainers.image.title="EjemploDesdeCero"
LABEL org.opencontainers.image.description="API Spring Boot — demo Docker"

RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

COPY --from=build /app/target/EjemploDesdeCero-*.jar app.jar

USER spring:spring

EXPOSE 8080

#ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]
