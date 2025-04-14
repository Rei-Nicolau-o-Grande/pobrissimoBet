FROM maven:3.9-amazoncorretto-21 as build
WORKDIR /app
COPY . .

RUN mkdir -p src/main/resources && \
    openssl genrsa -out src/main/resources/private.pem 2048 && \
    openssl rsa -in src/main/resources/private.pem -pubout -out src/main/resources/public.pem

RUN mvn clean install -DskipTests

FROM amazoncorretto:21-alpine
WORKDIR /app

COPY --from=build /app/target/ProbissimoBet.jar .
COPY --from=build /app/src/main/resources/*.pem ./src/main/resources/

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ProbissimoBet.jar"]
