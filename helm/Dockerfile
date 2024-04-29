FROM openjdk:17.0.1
VOLUME /tmp
COPY target/*.jar hello.jar
ENTRYPOINT ["java","-jar","/hello.jar"]