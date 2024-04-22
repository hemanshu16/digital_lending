FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY target/service-registry-0.0.1-SNAPSHOT.jar /opt/app
CMD ["java", "-jar", "/opt/app/service-registry-0.0.1-SNAPSHOT.jar"]