FROM amazoncorretto:21

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 5005

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "/app.jar"]