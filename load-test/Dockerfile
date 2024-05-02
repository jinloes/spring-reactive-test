FROM openjdk:17.0.1
VOLUME /tmp
WORKDIR /tmp
COPY target/load-test-1.0.jar load-test.jar
CMD ["java", "-jar", "load-test.jar", "-s", "com.jinloes.load_test.HelloSimulation", "-rf","results"]


