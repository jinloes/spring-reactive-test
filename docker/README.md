build image
docker build -t jinloes/spring-boot-docker-test ./build/docker/

or with gradle (better choice, also uploads the image)
gradle build buildDocker

push image
docker push jinloes/spring-boot-docker-test

run with
docker run -p 8080:8080 -t jinloes/spring-boot-docker-test
docker-compose up


