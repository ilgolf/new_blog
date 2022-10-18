## Dockerfile
FROM openjdk:11-jdk
EXPOSE 8089
ARG JAR_FILE=/build/libs/blog-0.0.1.jar
VOLUME ["/logs"]
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=dev","/app.jar"]