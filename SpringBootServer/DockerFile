FROM openjdk:17-jdk-alpine
WORKDIR /springbootserver
EXPOSE 8080
COPY ./target/*.jar springbootserver.jar
CMD ["java", "-jar", "./springbootserver.jar"]