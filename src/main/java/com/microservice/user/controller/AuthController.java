package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.security.jwt.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/logout")
    public ResponseEntity<String> postMethodName(@RequestHeader("Authorization") String authorization) {
        try {
            jwtUtils.removeTokenFromWhiteList(authorization.substring(7, authorization.length()));
            return ResponseEntity.ok().body("OK");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
