package com.devoteam.skillshapes.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/logging")
public class LogstashTester {

    private final Logger log = LoggerFactory.getLogger(LogstashTester.class);

    @GET
    @Path("/test")
    public String test(){
        log.info("This is a test of the log");
        return "test";
    }
}
