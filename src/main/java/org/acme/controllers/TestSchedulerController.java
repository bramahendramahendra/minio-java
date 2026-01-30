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
     * Trigger scheduler SKT secara manual dengan optional request ID
     * POST http://localhost:3010/test-scheduler/trigger-skt
     * Body: { "requestId": "REQ-001" } (optional)
     */
    @POST
    @Path("/trigger-skt")
    public Response triggerSKT(Map<String, String> payload) {
        try {
            String requestId = payload != null ? payload.get("pengajuan_id") : null;
            
            if (requestId != null && !requestId.isEmpty()) {
                LOG.info("Manual trigger scheduler SKT with request ID: " + requestId);
                iprocScheduled.send_tdr_skt_by_id_pengajuan(requestId);
            }
            //  else {
            //     LOG.info("Manual trigger scheduler SKT for all records...");
            //     iprocScheduled.send_tdr_skt();
            // }
            
            return Response.ok()
                .entity(Map.of(
                    "status", "success", 
                    "message", requestId != null ? 
                        "Scheduler SKT triggered successfully for request ID: " + requestId :
                        "Scheduler SKT triggered successfully for all records"
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

}