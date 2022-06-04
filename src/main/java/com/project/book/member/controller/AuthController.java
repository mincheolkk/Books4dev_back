package com.project.book.member.controller;

import com.project.book.common.config.jwt.AuthorizationExtractor;
import com.project.book.member.dto.TokenRequest;
import com.project.book.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/update/token")
    public ResponseEntity<?> updateAccessToken(@RequestBody TokenRequest tokenRequest, HttpServletRequest request) {
        String accessToken = AuthorizationExtractor.extract(request);
        String newAccessToken = authService.updateAccessToken(accessToken, tokenRequest);

        return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
    }
}
