# JHipster SkillShapes

Implementing a MS application that displays the Skill-Shapes of company employees using their CV data.

## Tools (Windows 10)

- [Visual Studio Code](https://code.visualstudio.com/docs/?dv=win)
- [IntelliJ ](https://www.jetbrains.com/idea/download/#section=windows)
- [Windows Terminal](https://www.microsoft.com/de-de/p/windows-terminal/9n0dx20hk701?rtc=1&activetab=pivot:overviewtab)
- [ Docker Desktop](https://hub.docker.com/editions/community/docker-ce-desktop-windows/)
- [Chrome](https://www.google.com/chrome/)

## Installation

Use [git](https://git-scm.com/downloads) to clone the repository.

```
git clone https://gitlab.devoteam.de/AB05105/skillshapes.git
git clone -b <branchname> <remote-repo-url>
```

## Configuration

Open your text editor as administrator (writing rights).

Make sure you select all data types when opening the folder.

`C:\Windows\System32\drivers\etc\hosts`

Add the following to map all jhipster services to local.

```
127.0.0.1 keycloak
127.0.0.1 jhipster-registry
127.0.0.1 gateway-mariadb
127.0.0.1 skillshapes-mariadb
```

## Usage for Local Development

Start keycloak, jhipster-registry and databases

```
docker-compose -f local-development.yml up -d
```

Start gateway application

```
cd gateway
mvn

Service listens on - localhost:8080

```

Start microservice application

```
cd microservice
.\mvnw compile quarkus:dev -Ddebug=5006

Service listens on - localhost:8081/q/dev

```

## Configure Docker service

Build docker images for gateway and microservice

````
cd microservice
./mvnw -Pprod clean package -DskipTests```

cd gateway
./mvnw package -Pprod verify jib:dockerBuild
````

This should have created 2 images gateway and skillshapes/microservice in docker.

## Usage for Dockerized Deployment

Start all applications

```
docker-compose up -d
```

### Service registry and configuration server:

- [JHipster Registry](http://localhost:8761)

### Applications and dependencies:

- gateway (SpringBoot gateway application)
- skillshapes (Quarkus microservice application)
- skillshapes's mariadb database

### Additional Services:

- [Keycloak server](http://localhost:9080)
- ELK Stack
