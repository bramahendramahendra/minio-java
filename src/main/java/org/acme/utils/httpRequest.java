package org.acme.utils;

import org.acme.utils.objectPost.RegistrationData;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import jakarta.inject.Singleton;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Singleton
public class httpRequest {
    

    private final Client client;
    // private final String username = "eproc";
    // private final String password = "eproc123";

    @ConfigProperty(name = "http.request.username")
    String username;
    
    @ConfigProperty(name = "http.request.password")
    String password;

    public httpRequest() {
        this.client = ClientBuilder.newClient();
    }

    public responseConsume postData(String url,  RegistrationData request) {
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeader = "Basic " + encodedAuth;
        Response response = client.target(url)
            .request(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, authHeader)
            .post(Entity.entity(request, MediaType.APPLICATION_JSON));
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String a = objectMapper.writeValueAsString(response.getEntity());
            String b = objectMapper.writeValueAsString(request);
            // System.out.println("request = "+ b);
            // System.out.println("data = "+a);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
        
        // RegistrationData reg = objectMapper.readValue(request, RegistrationData.class);
        // System.out.println(response.getStatus());
        
        // try {
        //     String jsonString = objectMapper.writeValueAsString(response);
        //     // System.out.println(jsonString);
        // } catch (Exception e) {
        //     // TODO: handle exception
        //     System.err.println(e);
        // }
        
        return response.readEntity(responseConsume.class);
    }



}
