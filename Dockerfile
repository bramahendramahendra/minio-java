# syntax=docker/dockerfile:1

################################################################################
# Create a stage for resolving and downloading dependencies.
################################################################################
FROM eclipse-temurin:22-jdk-jammy as deps

WORKDIR /build

# Copy the mvnw wrapper with executable permissions.
COPY mvnw mvnw
COPY .mvn/ .mvn/

# Fix line endings and make executable
RUN dos2unix mvnw || sed -i 's/\r$//' mvnw && chmod +x mvnw

# Download dependencies as a separate step to take advantage of Docker's caching.
# Leverage a cache mount to /root/.m2 so that subsequent builds don't have to
# re-download packages.
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -DskipTests

################################################################################
# Create a stage for building the application
################################################################################
FROM deps as package

WORKDIR /build

COPY ./src src/
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests

################################################################################
# Create runtime stage
################################################################################
FROM eclipse-temurin:22-jre-jammy AS final

# Create a non-privileged user that the app will run under.
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser

WORKDIR /deployments

# Copy Quarkus app structure from package stage
COPY --chown=appuser:appuser --from=package /build/target/quarkus-app/lib/ /deployments/lib/
COPY --chown=appuser:appuser --from=package /build/target/quarkus-app/*.jar /deployments/
COPY --chown=appuser:appuser --from=package /build/target/quarkus-app/app/ /deployments/app/
COPY --chown=appuser:appuser --from=package /build/target/quarkus-app/quarkus/ /deployments/quarkus/

# Buat folder tmp untuk file upload dengan permission yang benar
RUN mkdir -p /deployments/tmp && chown -R appuser:appuser /deployments/tmp

USER appuser

EXPOSE 3010

ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0"

ENTRYPOINT ["java", "-jar", "/deployments/quarkus-run.jar"]