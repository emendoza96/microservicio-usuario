package com.microservice.user.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.user.dao.redis.WhiteListTokenRepository;
import com.microservice.user.domain.WhiteListToken;
import com.microservice.user.service.WhiteListTokenService;

@Service
public class WhiteListTokenServiceImpl implements WhiteListTokenService {

    @Autowired
    private WhiteListTokenRepository whiteListTokenRepository;

    @Override
    public void addTokenToWhiteList(String username, String token) {
        WhiteListToken wToken = new WhiteListToken(username, token);
        whiteListTokenRepository.save(wToken);
    }

    @Override
    public boolean isTokenInWhiteList(String username, String token) {
        Optional<WhiteListToken> wToken = whiteListTokenRepository.findById(username);
        return wToken.isPresent() && wToken.get().getToken().equals(token);
    }

    @Override
    public void removeTokenFromWhiteList(String username, String token) {
        Optional<WhiteListToken> wToken = whiteListTokenRepository.findById(username);
        if (wToken.isPresent() && wToken.get().getToken().equals(token)) {
            whiteListTokenRepository.deleteById(username);
        }
    }

}
