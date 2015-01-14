package com.intelligrape.seeme.model;

/**
 * Base classs for all the Model, It has status and message variable which we are getting common in all API response.
 */
public class Model {
    private int status, code;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
