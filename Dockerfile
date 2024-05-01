FROM quay.io/keycloak/keycloak as builder



# Enable health and metrics support

ENV KC_HEALTH_ENABLED=true

ENV KC_METRICS_ENABLED=true

ENV KC_FEATURES=token-exchange


# Configure a database vendor




WORKDIR /opt/keycloak



RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore

RUN /opt/keycloak/bin/kc.sh build



FROM quay.io/keycloak/keycloak

COPY --from=builder /opt/keycloak/ /opt/keycloak/

# Install the custom theme
# COPY selfonboarding /opt/keycloak/themes/selfonboarding

WORKDIR /opt/keycloak

# configuring email listener to send emails for specific events
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start"]
