package org.acme.controllers.upload_minio.dto;

public class CheckDirectoryDto {
    private String urlPath;
    private String nameFile;
    
    public String getUrlPath() {
        return urlPath;
    }
    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }
    public String getNameFile() {
        return nameFile;
    }
    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }
    
}
