package com.microservice.user.dao.redis;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.user.domain.WhiteListToken;

public interface WhiteListTokenRepository extends JpaRepository<WhiteListToken, String>{
    void deleteByToken(String token);
    boolean existsByToken(String token);
}
