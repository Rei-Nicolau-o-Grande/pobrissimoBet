FROM amazoncorretto:21-alpine
WORKDIR /app
COPY target/ProbissimoBet.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","ProbissimoBet.jar"]