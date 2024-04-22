FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY target/loan-service.jar /opt/app
CMD ["java", "-jar", "/opt/app/loan-service.jar --spring.profiles.active=prod"]