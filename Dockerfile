FROM maven:3-jdk-8 as builder
WORKDIR /app
COPY . /app
RUN mvn package

FROM openjdk:8
ARG APP_VERSION=1.0
COPY --from=builder "/app/target/podyn-${APP_VERSION}.jar" /app/podyn.jar
COPY entrypoint.sh /
