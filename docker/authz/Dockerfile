FROM openjdk:8-jdk-alpine

ARG JAR_FILE
ADD ${JAR_FILE} /app/

CMD ["java", "-Xmx200m", "-jar", "/app/authz.jar"]

EXPOSE 5000