package com.devoteam.skillshapes.web.rest;

import io.quarkus.eureka.config.DefaultInstanceInfoContext;
import io.quarkus.eureka.config.EurekaRuntimeConfiguration;
import io.quarkus.eureka.config.InstanceInfoContext;
import io.quarkus.eureka.exception.HealthCheckException;
import io.quarkus.runtime.annotations.ConfigItem;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import io.quarkus.eureka.client.Status;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.ws.rs.core.Response.Status.Family.CLIENT_ERROR;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;

@Liveness
@Path("/info")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HealthResource implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("I'm alive");
    }

    @GET
    @Path("/health")
    public Response health() {
        return Response.ok(Map.of("STATUS", "UP")).build();
    }

    @GET
    @Path("/status")
    public Response status() {
        return Response.ok(Map.of()).build();
    }

    private final Logger log = LoggerFactory.getLogger(SkillShapeResource.class);

    @GET
    @Path("/check")
    public Response healthCheck(@QueryParam("url") String healthCheckUrl) {
        Client client = ResteasyClientBuilder.newClient();
        try {
            log.info("Health Check for: {}",healthCheckUrl);
            Response response = client.target(healthCheckUrl)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
            if(response != null){
                log.info("Response: {}",response.getHeaders().toString());
                log.info("Response Status: {}",response.getStatusInfo());
            }
            if (response.getStatusInfo().getFamily().equals(CLIENT_ERROR)) {
                throw new HealthCheckException(
                    "Instance can't reach own application health check. Ensure this has been implemented"
                );
            }

            Status status = getStatusFromResponse(response);

            return Response.ok(Map.of("STATUS",status.toString())).build();

        } catch (ProcessingException ex) {
            throw new HealthCheckException("Health check not reachable: "+healthCheckUrl, ex);
        } finally {
            client.close();
        }
    }

    private Status getStatusFromResponse(final Response response) {

        if (!response.getStatusInfo().getFamily().equals(SUCCESSFUL)) {
            return Status.DOWN;
        }

        final Map<String, String> body = response.readEntity(Map.class);
        log.info("Health Check GetStatusFromResponse: {}", body);
        log.info("Health Check GetStatusFromResponse Keyset: {}", body.keySet());
        return body.entrySet()
            .stream()
            .filter(e -> e.getKey().equalsIgnoreCase("status"))
            .map(Map.Entry::getValue)
            .map(String::toUpperCase)
            .map(Status::valueOf)
            .findFirst().orElse(Status.UNKNOWN);
    }

}

