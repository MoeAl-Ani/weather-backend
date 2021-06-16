FROM openjdk:11-jre-slim-stretch
WORKDIR /

COPY ["target/weather.jar", "weather.jar"]
EXPOSE 8080
CMD sleep 5 && java -jar weather.jar
