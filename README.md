# JHipster SkillShapes

Implementing a MS application that displays the Skill-Shapes of company employees using their CV data.

https://confluence.devoteam.de/display/SKIL/Microservice+Architecture

![alt text](skillshape.png "SkillShape")

## Tools (Windows 10)

- [Visual Studio Code](https://code.visualstudio.com/docs/?dv=win)
- [IntelliJ ](https://www.jetbrains.com/idea/download/#section=windows)
- [Windows Terminal](https://www.microsoft.com/de-de/p/windows-terminal/9n0dx20hk701?rtc=1&activetab=pivot:overviewtab)
- [Docker Desktop](https://hub.docker.com/editions/community/docker-ce-desktop-windows/)
- [Chrome](https://www.google.com/chrome/)
- [mvn](https://maven.apache.org/guides/getting-started/windows-prerequisites.html)

## Installation

Use [git](https://git-scm.com/downloads) to clone the repository - branch master.

```sh
git clone https://gitlab.devoteam.de/AB05105/skillshapes.git
```

> **Note**: If your git clone is not working make sure you are in the VPN. <br>
> If you continue to have problems add the following to your hosts file.

> C:\Windows\System32\drivers\etc\hosts
>
> ```sh
> # Gitlab
> 10.99.70.40 gitlab.devoteam.de
> ```
>
> If the problem still persists please contact the team.

## Local Development Configuration

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
```

# Local Development Instructions

> **Note**: Please make sure you have [Docker Desktop](https://hub.docker.com/editions/community/docker-ce-desktop-windows/) installed.
> Check out the tools section if you have not prepared your environment..

1. #### Start keycloak, jhipster-registry and database
   ```sh
   docker-compose -f local-development.yml up -d
   ```
2. #### Start ELK Stack, Elastic Search, Logstash, Kibana and Jaeger Tracing
   ```
   docker-compose -f elkstack.yml up -d
   ```

## Start microservice application

> **Note**: The gateway uses port 8080. Thus we manually update the microservice properties to use 8081. <br>
> This step is not needed in a dockerized setup.

Update the following in: microservice/src/main/resources/application.properties

```sh
quarkus.http.port=8081
```

Then navigate to the microservice and run it.

```sh
cd microservice
.\mvnw compile quarkus:dev -Ddebug=5006
```

Connect to [http://localhost:8081/q/dev](http://localhost:8081/q/dev)

## Start gateway application

```sh
cd gateway
mvn
```

Connect to [http://localhost:8080](http://localhost:8080)

# Dockerized Local Deployment

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

## Applications when running:

- [Gateway](localhost:8080)
- [Microservice](http://localhost:8081/q/swagger-ui/)
- [JHipster Registry](http://localhost:8761)
- [Keycloak](http://localhost:9080/)
- [Kibana Dashboard](http://localhost:5601)
- [Jaeger Dashboard](http://localhost:16686)

## Logging with ELK Stack & Tracing with Jaeger:

Start elastic search, logstash, kibana and jaegertracing

```sh
docker-compose -f elkstack.yml up -d
```

### Kibana

- Go to [Kibana Dashboard](http://localhost:5601)
- Click on Kibana (visualize & analyze) and select _create index pattern_ (bottom).
- Type in "logstash-\*" and click next step.
- Use filter "@timestamp" and click create index pattern.
- Go to discover tab.

### Jaeger

- Go to [Jaeger Dashboard](http://localhost:16686)
- Make requests to the microservice and see the Trace-ID in Response Headers
- Use Trace-ID in Jaeger or Kibana after creating Jaeger Index

## CI / CD

- Working on Jenkins integration (in development)

## What we have now

Working application setup with gateway, microservice, registry, authentication, logging, tracing

- Generated gateway
- Generated microservice
- Entity generation
- JDL import
- ELK Stack running
- Jaeger Open Tracing running

## Troubleshooting

### Docker Push Execution Error on Build

If there is an error 'Execution of docker push' skip it and make sure the image exists in your docker environment. It should be there.

### Docker cannot start service

```sh
ERROR: for keycloak Cannot start service keycloak: error while creating mount source path '/run/desktop/mnt/host/c/Users/HL05475/Documents/Projekte/Cloud-Testing/skillshapes/keycloak-db': mkdir /run/desktop/mnt/host/c: file exists

ERROR: for jhipster-registry Cannot start service jhipster-registry: error while creating mount source path '/run/desktop/mnt/host/c/Users/HL05475/Documents/Projekte/Cloud-Testing/skillshapes/central-server-config': mkdir /run/desktop/mnt/host/c: file exists
```

Delete failed images / containers, kill all docker processes and restart docker entirely.

### Project Contacts

- Andreas Brust (Competence Lead / Product Owner) - andreas.brust@devoteam.com
- Can Saner (Backend Development) - ahmet.can.saner@devoteam.com
- David Minkovski (Development & Microservice Orchestration) - david.minkovski@devoteam.com
- Severin Koch (Frontend Development) - severin.koch@devoteam.com
