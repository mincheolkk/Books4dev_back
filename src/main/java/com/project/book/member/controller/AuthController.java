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
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;


    @PostMapping(value = "/update/token")
    public ResponseEntity<?> updateAccessToken(@LoginMember final Member member, @RequestBody final TokenRequest tokenRequest) {
        String newAccessToken = authService.updateAccessToken(tokenRequest, member);

        return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
    }

    @GetMapping(value = "/out")
    public ResponseEntity<?> logOut(final HttpServletRequest request) {
        authService.logOut(request);

        return ResponseEntity.noContent().build();
    }


    @GetMapping(value = "/test/token")
    public ResponseEntity<?> kk(@LoginMember final Member member) {
        ResponseEntity refresh = authService.getRefresh(member);
        return ResponseEntity.ok(refresh.getBody());
    }
}