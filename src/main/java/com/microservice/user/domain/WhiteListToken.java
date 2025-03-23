package com.microservice.user.domain;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.annotation.Id;

@RedisHash("WhiteListToken")
public class WhiteListToken {

    @Id
    private String username;
    private String token;

    public WhiteListToken() {}

    public WhiteListToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "WhiteListToken [username=" + username + ", token=" + token + "]";
    }

}



