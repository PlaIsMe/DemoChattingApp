FROM openjdk:17-jdk-alpine

WORKDIR /socketserver
COPY ./target/*.jar socketserver.jar
EXPOSE 8081

CMD ["java", "-cp", "socketserver.jar", "com.chattingapplication.Main"]