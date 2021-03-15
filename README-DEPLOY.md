# Dockerized Deployment

1. Build docker image for microservice

   > **Note**: Make sure to check quarkus.container-image.registry in the properties. It should have no http:// or https://.

   ```sh
   cd microservice
   ./mvnw -Pprod clean package -DskipTests
   ```

2. Build docker image for gateway

   > **Note**: Make sure to check the jib-maven-plugin in the pom.xml to image target registry.

   ```sh
   cd gateway
   ./mvnw package -Pprod verify jib:dockerBuild
   ```

3. Check docker for created images: **gateway** and **skillshapes/microservice**

4. Start all applications

   ```sh
   docker-compose up -d
   ```

   #fh22TKt3yKcrrGhsQFd7
