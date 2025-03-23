package com.microservice.user.service;

public interface WhiteListTokenService {
    void addTokenToWhiteList(String username, String token);
    boolean isTokenInWhiteList(String username, String token);
    void removeTokenFromWhiteList(String username, String token);
}
