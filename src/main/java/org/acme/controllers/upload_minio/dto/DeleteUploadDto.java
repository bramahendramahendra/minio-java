package org.acme.controllers.upload_minio.dto;

import jakarta.ws.rs.FormParam;

public class DeleteUploadDto {
    // @FormParam("url_minio")
    private String url_minio;

    public String getUrl_minio() {
        return url_minio;
    }

    public void setUrl_minio(String url_minio) {
        this.url_minio = url_minio;
    }
    
}
