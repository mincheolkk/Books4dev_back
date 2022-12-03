FROM openjdk:11-jdk

VOLUME /tmp

ARG JAR_FILE=./build/libs/book-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]