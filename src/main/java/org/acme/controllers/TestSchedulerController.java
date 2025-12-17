package org.acme.controllers;

import org.acme.scheduler.IprocScheduled;
import org.jboss.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Path("/test-scheduler")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestSchedulerController {

    private static final Logger LOG = Logger.getLogger(TestSchedulerController.class);

    @Inject
    IprocScheduled iprocScheduled;

    /**
     * Trigger scheduler SKT secara manual
     * POST http://localhost:3010/test-scheduler/trigger-skt
     */
    @POST
    @Path("/trigger-skt")
    public Response triggerSKT() {
        try {
            LOG.info("Manual trigger scheduler SKT...");
            iprocScheduled.send_tdr_iproc_skt();
            return Response.ok()
                .entity(Map.of(
                    "status", "success", 
                    "message", "Scheduler SKT triggered successfully"
                ))
                .build();
        } catch (Exception e) {
            LOG.error("Error triggering scheduler SKT: " + e.getMessage());
            return Response.status(500)
                .entity(Map.of(
                    "status", "error", 
                    "message", e.getMessage()
                ))
                .build();
        }
    }

    /**
     * Trigger scheduler Non-SKT secara manual
     * POST http://localhost:3010/test-scheduler/trigger-nonskt
     */
    @POST
    @Path("/trigger-nonskt")
    public Response triggerNonSKT() {
        try {
            LOG.info("Manual trigger scheduler Non-SKT...");
            iprocScheduled.send_tdr_iproc_nonskt();
            return Response.ok()
                .entity(Map.of(
                    "status", "success", 
                    "message", "Scheduler Non-SKT triggered successfully"
                ))
                .build();
        } catch (Exception e) {
            LOG.error("Error triggering scheduler Non-SKT: " + e.getMessage());
            return Response.status(500)
                .entity(Map.of(
                    "status", "error", 
                    "message", e.getMessage()
                ))
                .build();
        }
    }
}