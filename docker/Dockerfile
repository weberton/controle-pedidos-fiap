FROM amazoncorretto:21

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

COPY docker/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

ENTRYPOINT ["/wait-for-it.sh", "mysql:3306", "--", "java", "-jar", "/app.jar"]


