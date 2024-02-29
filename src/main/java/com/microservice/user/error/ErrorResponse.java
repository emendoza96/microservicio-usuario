package com.microservice.user.error;

public class ErrorResponse {

    private ErrorDetails error;

    public ErrorResponse() {}

    public ErrorResponse(ErrorDetails error) {
        this.error = error;
    }

    public ErrorDetails getError() {
        return error;
    }

    public void setError(ErrorDetails error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ErrorResponse [error=" + error + "]";
    }

}
