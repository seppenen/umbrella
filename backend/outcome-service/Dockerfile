FROM openjdk:17-jdk-alpine
VOLUME /tmp
ADD ./target/outcome-service-1.0-SNAPSHOT.jar app.jar
COPY ./Entrypoint.sh /
RUN chmod +x /Entrypoint.sh
ENTRYPOINT ["/bin/sh","/Entrypoint.sh"]
