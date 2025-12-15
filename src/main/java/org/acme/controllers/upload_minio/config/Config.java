package org.acme.controllers.upload_minio.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

// import jakarta.resource.spi.ConfigProperty;

public class Config {
    // @ConfigProperty()
    @ConfigProperty(name = "greeting.name")
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
