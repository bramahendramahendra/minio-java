package org.acme.controllers.upload_minio.dto;

import jakarta.validation.constraints.NotBlank;

public class UploadDto {
    
    @NotBlank(message="Path file tidak boleh kosong")
    private String pathUrl;

    @NotBlank(message="bucket tidak boleh kosong")
    private String bucket;

    @NotBlank(message="object tidak boleh kosong")
    private String objek;

    public UploadDto(){

    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getObjek() {
        return objek;
    }

    public void setObjek(String objek) {
        this.objek = objek;
    }
    
    
    
    
}
