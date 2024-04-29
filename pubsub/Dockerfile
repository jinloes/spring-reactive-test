FROM openjdk:17.0.1
VOLUME /tmp
COPY target/*.jar pubsub.jar
ENTRYPOINT ["java","-jar","/pubsub.jar"]