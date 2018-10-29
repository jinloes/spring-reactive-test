build image
docker build -t jinloes/spring-boot-docker-test ./src/main/docker/

or with gradle (better choice, also uploads the image)
gradle build buildDocker

or with maven
mvn install

push image
docker push jinloes/spring-boot-docker-test

run with
docker run -p 8080:8080 -t jinloes/spring-boot-docker-test
docker-compose up


To Run: 
docker-compose -f ./docker-compose.yml up


