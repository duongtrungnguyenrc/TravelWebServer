package com.web.travel.payload.response;

public class MessageResponse {
    //0: Wrong information
    //1: Ok
    private int status;
    private String message;

    public MessageResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
