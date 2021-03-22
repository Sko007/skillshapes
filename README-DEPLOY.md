# Dockerized Deployment

## Configuration

Open your text editor as administrator (writing rights).

Make sure you select all data types when opening the folder.

```
C:\Windows\System32\drivers\etc\hosts
```

Add the following to map all jhipster services to local.

```sh
127.0.0.1 keycloak
127.0.0.1 jhipster-registry
127.0.0.1 skillshapes-mariadb
127.0.0.1 elasticsearch
127.0.0.1 jaeger
127.0.0.1 microservice
127.0.0.1 logstash
```

## Docker Image building

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

3. Check docker for created images: **gateway** and **microservice**

## Docker compose

Start all applications

```sh
docker-compose up -d
```

## Troubleshooting

### Microservice not reachable in Registry - Health check error

Restart the microservice
