package org.acme.controllers.upload_minio.dto;

public class UploaderDto {
    private boolean status;
    private String Result;
    
    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getResult() {
        return Result;
    }
    public void setResult(String result) {
        Result = result;
    }

    
}
