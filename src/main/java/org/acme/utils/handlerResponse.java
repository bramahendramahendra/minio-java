package org.acme.utils;

public class handlerResponse {

    private String responseMessage;
    private String responseCode;
    private Object responseData;

    
    public handlerResponse(String responseMessage, String responseCode, Object responseData){
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
        this.responseData = responseData;
    }


    public String getResponseMessage() {
        return responseMessage;
    }


    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }


    public String getResponseCode() {
        return responseCode;
    }


    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }


    public Object getResponseData() {
        return responseData;
    }


    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }

}
