FROM openjdk:17-jdk-alpine
RUN apk add --no-cache curl
VOLUME /tmp
ADD ./target/eureka-0.0.1-SNAPSHOT.jar app.jar
COPY ./Entrypoint.sh /
RUN chmod +x /Entrypoint.sh
ENTRYPOINT ["/bin/sh","/Entrypoint.sh"]
