package com.microservice.user.dao.redis;

import org.springframework.data.repository.CrudRepository;

import com.microservice.user.domain.WhiteListToken;

public interface WhiteListTokenRepository extends CrudRepository<WhiteListToken, String>{

}
