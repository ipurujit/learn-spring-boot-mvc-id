FROM openjdk:11.0.16-jdk
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE
COPY target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]