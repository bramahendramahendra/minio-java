package org.acme.controllers.upload_minio.dto;

import jakarta.validation.constraints.NotBlank;

public class GetObjectPerbankanDto {

    @NotBlank(message = "url Minio must be required")
    private String url_minio;

    public String getUrl_minio() {
        return url_minio;
    }

    public void setUrl_minio(String url_minio) {
        this.url_minio = url_minio;
    }
    
}
