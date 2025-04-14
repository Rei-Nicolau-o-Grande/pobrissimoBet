FROM amazoncorretto:21-alpine
COPY . .
RUN ./mvn clean install -DskipTests
EXPOSE 8080
ENTRYPOINT ["java","-jar","targer/ProbissimoBet.jar"]