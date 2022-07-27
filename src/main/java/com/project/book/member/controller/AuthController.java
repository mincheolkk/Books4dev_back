package com.project.book.member.controller;

import com.project.book.common.config.jwt.AuthorizationExtractor;
import com.project.book.common.config.jwt.LoginMember;
import com.project.book.member.domain.Member;
import com.project.book.member.dto.TokenRequest;
import com.project.book.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/update/token")
    public ResponseEntity<?> updateAccessToken(@RequestBody TokenRequest tokenRequest, HttpServletRequest request) {
        String accessToken = AuthorizationExtractor.extract(request);
        String newAccessToken = authService.updateAccessToken(tokenRequest, request);

        return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
    }

    @PostMapping(value = "/out")
    public ResponseEntity<?> logOut(HttpServletRequest request) {
        System.out.println("223123123");
        authService.logOut(request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/select")
    public ResponseEntity<?> dda(HttpServletRequest request) {
        String authorization = request.getHeader("refreshToken");
        System.out.println("authorization = " + authorization);
        String temp = "tmep temp!";
        return ResponseEntity.ok(temp);
    }

    @GetMapping(value = "/test/token")
    public void kk(@LoginMember Member member) {
        System.out.println("member = " + member.getId());
    }
}