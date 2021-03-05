# Dockerized Deployment

1. Build docker image for microservice

   ```sh
   cd microservice
   ./mvnw -Pprod clean package -DskipTests
   ```

2. Build docker image for gateway

   ```sh
   cd gateway
   ./mvnw package -Pprod verify jib:dockerBuild
   ```

3. Check docker for created images: **gateway** and **skillshapes/microservice**

4. Start all applications

   ```sh
   docker-compose up -d
   ```
