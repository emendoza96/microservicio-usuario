package com.microservice.user.service.impl;

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
        WhiteListToken wToken = new WhiteListToken(token, token);
        whiteListTokenRepository.save(wToken);
    }

    @Override
    public boolean isTokenInWhiteList(String token) {
        return whiteListTokenRepository.existsByToken(token);
    }

    @Override
    public void removeTokenFromWhiteList(String token) {
        whiteListTokenRepository.deleteByToken(token);
    }

}
