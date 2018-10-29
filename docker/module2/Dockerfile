# Select java version
FROM openjdk:8-jdk-alpine
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/docker-1.0.jar"]
VOLUME /tmp
# Copy jar file
ARG JAR_FILE
ADD ${JAR_FILE} /module2.jar
RUN sh -c 'touch /module2.jar'