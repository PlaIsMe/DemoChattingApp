FROM openjdk:17-alpine

WORKDIR /socketserver
COPY ./server/target/*.jar ./socket_server.jar

CMD ["java", "-jar", "socket_server.jar"]