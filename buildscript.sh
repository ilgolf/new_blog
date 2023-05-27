# Docker build

echo "build jar file ..."
./gradlew clean bootJar

echo "build docker image ..."
docker build --platform linux/amd64 -t ilgolf/blog:latest .

echo "push docker image ..."
docker push ilgolf/blog:latest

echo "remove docker image ..."
docker rmi -f ilgolf/blog:latest
