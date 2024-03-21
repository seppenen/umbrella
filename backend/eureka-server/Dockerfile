FROM openjdk:17-jdk-alpine
VOLUME /tmp
ADD ./target/eureka-server-0.0.1-SNAPSHOT.jar app.jar
COPY ./Entrypoint.sh /
RUN chmod +x /Entrypoint.sh
ENTRYPOINT ["/bin/sh","/Entrypoint.sh"]
