FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY target/user-service.jar /opt/app
CMD ["java", "-jar", "/opt/app/user-service.jar --spring.profiles.active=dev"]