# Build the app
FROM maven:3.8.1-openjdk-17 AS BUILD
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Create final image
FROM openjdk:17.0.1-slim

# Install dependencies for Chrome and ChromeDriver
RUN apt-get update && apt-get install -y wget unzip curl gnupg --no-install-recommends \
    && wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get -y install google-chrome-stable \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Set the display port to avoid issues in headless mode
ENV DISPLAY=:99

WORKDIR /app
COPY --from=BUILD /app/target/sampling-server-0.0.1-SNAPSHOT.jar server.jar

# Add WebDriverManager dependency
RUN apt-get update && apt-get install -y unzip
COPY resources/WebDriverManager.jar /app/lib/

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "server.jar"]