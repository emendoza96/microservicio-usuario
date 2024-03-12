package com.microservice.user.error;

public class ErrorResponse {

    private ErrorDetail error;

    public ErrorResponse() {}

    public ErrorResponse(ErrorDetail error) {
        this.error = error;
    }

    public ErrorDetail getError() {
        return error;
    }

    public void setError(ErrorDetail error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ErrorResponse [error=" + error + "]";
    }

}
