# Build the app
FROM maven:3.8.1-openjdk-17 AS BUILD
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Create final image
FROM openjdk:17.0.1-slim

# Install dependencies for Chrome
RUN apt-get update && apt-get install -y wget unzip xvfb libxi6 libgconf-2-4 libnss3-dev libxss1 libappindicator1 libindicator7

# Install Chrome
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable

# Install ChromeDriver
RUN wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/$(curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE)/chromedriver_linux64.zip \
    && unzip /tmp/chromedriver.zip -d /usr/local/bin/ \
    && rm /tmp/chromedriver.zip

# Set display environment variable
ENV DISPLAY=:99

WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=BUILD /app/target/sampling-server-0.0.1-SNAPSHOT.jar sampling.jar

EXPOSE 8080

# Start Xvfb in the background and run the jar file
ENTRYPOINT ["sh", "-c", "Xvfb :99 -ac & java -jar sampling.jar"]
