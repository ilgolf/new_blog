app_name=blog
image_name=ilgolf/blog:latest

echo "docker stop container ..."

docker stop $app_name

echo "docker remove container ..."

docker rm $(docker ps -a -q)

echo "docker remove image ..."

docker rmi $image_name

echo "deploy with compose ..."

docker-compose up -d app