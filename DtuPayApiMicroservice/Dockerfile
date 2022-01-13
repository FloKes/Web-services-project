FROM adoptopenjdk:11-jre-hotspot
COPY target/lib /usr/src/lib
COPY target/dtuPayApiMicroservice-1.0.0.jar /usr/src/
WORKDIR /usr/src/
CMD java -Xmx64m -jar dtuPayApiMicroservice-1.0.0.jar
