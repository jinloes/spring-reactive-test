Build 

mvn clean package
docker buildx build --tag hello:latest .

Helm

helm install hello ./hello