package org.acme.utils;

public class responseConsume {
    private String message;
    private String version;
    private String exception;
    private Object data;
    private Object validation;
    

    

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public Object getValidation() {
        return validation;
    }
    public void setValidation(Object validation) {
        this.validation = validation;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public String getException() {
        return exception;
    }
    public void setException(String exception) {
        this.exception = exception;
    }

    
}
