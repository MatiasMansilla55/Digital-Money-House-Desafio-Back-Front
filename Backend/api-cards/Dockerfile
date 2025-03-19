FROM openjdk:21
ARG JAR_FILE=target/api-cards-0.0.1.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8083