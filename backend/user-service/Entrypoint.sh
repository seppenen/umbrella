#!/bin/sh

exec java -agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n -jar /app.jar
