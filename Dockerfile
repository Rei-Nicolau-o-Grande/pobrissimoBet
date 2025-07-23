FROM maven:3.9.11-eclipse-temurin-21-alpine AS build

RUN apk add --no-cache openssl

WORKDIR /app

COPY . .

RUN mkdir -p src/main/resources && \
    openssl genrsa -out src/main/resources/private.pem 2048 && \
    openssl rsa -in src/main/resources/private.pem -pubout -out src/main/resources/public.pem

RUN cp src/main/resources/data.sql.example src/main/resources/data.sql

RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/ProbissimoBet.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ProbissimoBet.jar"]
