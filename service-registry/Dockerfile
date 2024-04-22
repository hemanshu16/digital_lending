FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY target/service-registry.jar /opt/app
CMD ["java", "-jar", "/opt/app/service-registry.jar" , "--spring.profiles.active=prod"]