FROM maven:3.9-eclipse-temurin-24 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:9.0.109-jdk25
WORKDIR /app
# Удаляем дефолтные приложения
RUN rm -rf /usr/local/tomcat/webapps/*
ARG CONTEXT_NAME
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/${CONTEXT_NAME}.war
EXPOSE 8080