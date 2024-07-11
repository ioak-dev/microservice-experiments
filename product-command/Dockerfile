FROM debian:buster-slim as packager

# Install dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    wget \
    curl \
    telnet \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Download and install Eclipse Temurin OpenJDK 21
RUN mkdir -p /usr/local/openjdk-21 \
    && wget -qO- https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21%2B35/OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz | tar -xz -C /usr/local/openjdk-21 --strip-components 1

# Set environment variables for Java
ENV JAVA_HOME=/usr/local/openjdk-21
ENV PATH=$JAVA_HOME/bin:$PATH

# Set the working directory
WORKDIR /app

# Copy the Spring Boot application JAR file into the container
COPY build/libs/product-*.jar /opt/app.jar

ENV MONGODB_URI mongodb://localhost:27017
ENV MONGODB_DB shopping-cart
ENV KAFKA_SERVER kafka:9092

# Expose the port that the application will run on
EXPOSE 8088


# Define the entry point for the container
ENTRYPOINT ["java", "-jar", "/opt/app.jar"]
