FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
COPY target/preordering-0.0.1-SNAPSHOT.jar preordering.jar
ENTRYPOINT ["java","-jar","/preordering.jar"]