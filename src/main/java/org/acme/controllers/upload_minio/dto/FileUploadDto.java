package org.acme.controllers.upload_minio.dto;


import org.jboss.resteasy.reactive.PartType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

import java.io.InputStream;



public class FileUploadDto {

    @NotBlank(message = "filename not blank")
    @FormParam("file_name")
    @PartType(MediaType.TEXT_PLAIN)
    private String fileName;

    @NotBlank(message = "id_vendor not blank")
    @FormParam("id_vendor")
    @PartType(MediaType.TEXT_PLAIN)
    private String idVendor;

    @NotNull(message = "file_upload not null")
    @FormParam("file_upload")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream fileData;

    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public InputStream getFileData() {
        return fileData;
    }

    public void setFileData(InputStream fileData) {
        this.fileData = fileData;
    }

    public String getIdVendor() {
        return idVendor;
    }

    public void setNamaVendor(String idVendor) {
        this.idVendor = idVendor;
    }



    
}
