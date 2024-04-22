FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY target/bre-service.jar /opt/app
CMD ["java", "-jar", "/opt/app/bre-service.jar"]