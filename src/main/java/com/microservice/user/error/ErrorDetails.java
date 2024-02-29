package com.microservice.user.error;

import java.util.LinkedHashMap;
import java.util.Map;

public class ErrorDetails {

    private Integer code;
    private String message;
    private Map<String, String> details = new LinkedHashMap<String, String>();

    public ErrorDetails() {}

    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Map<String, String> getDetails() {
        return details;
    }
    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "ErrorDetails [code=" + code + ", message=" + message + ", details=" + details + "]";
    }

}
