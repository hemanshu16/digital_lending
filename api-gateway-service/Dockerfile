FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY target/api-gateway.jar /opt/app
CMD ["java", "-jar", "/opt/app/api-gateway.jar --spring.profiles.active=prod"]