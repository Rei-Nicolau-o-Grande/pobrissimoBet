FROM maven:3.9-amazoncorretto-21 as build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests


FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/target/ProbissimoBet.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ProbissimoBet.jar"]
