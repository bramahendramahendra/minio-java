package org.acme;

import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.servers.Servers;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@Servers(value = {
    @Server(url = "/"),
    @Server(url = "/tdr-services-minio")
})
@ApplicationPath("/")
public class MainApp extends Application {}
