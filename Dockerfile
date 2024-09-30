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
    && CHROME_VERSION=$(google-chrome --version | grep -oP '\d+\.\d+\.\d+\.\d+') \
    && CHROMEDRIVER_VERSION=$(curl -sS https://chromedriver.storage.googleapis.com/LATEST_RELEASE_$CHROME_VERSION) \
    && wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/$CHROMEDRIVER_VERSION/chromedriver_linux64.zip \
    && unzip /tmp/chromedriver.zip -d /usr/local/bin/ \
    && rm /tmp/chromedriver.zip \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Set the display port to avoid issues in headless mode
ENV DISPLAY=:99

WORKDIR /app
COPY --from=BUILD /app/target/sampling-server-0.0.1-SNAPSHOT.jar server.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "server.jar"]
