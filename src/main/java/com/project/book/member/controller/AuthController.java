package com.project.book.member.controller;

import com.project.book.common.config.jwt.LoginMember;
import com.project.book.member.domain.Member;
import com.project.book.member.dto.TokenRequest;
import com.project.book.member.dto.response.MemberResponse;
import com.project.book.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMember(@LoginMember final Member member) {
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PostMapping(value = "/update/token")
    public ResponseEntity<?> updateAccessToken(@LoginMember final Member member, @RequestBody @Valid final TokenRequest tokenRequest) {
        String newAccessToken = authService.updateAccessToken(member, tokenRequest);

        return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
    }

    @GetMapping(value = "/out")
    public ResponseEntity<?> logOut(final HttpServletRequest request) {
        authService.logOut(request);

        return ResponseEntity.noContent().build();
    }


    @GetMapping(value = "/refreshtoken")
    public ResponseEntity<?> getRefreshToken(final HttpServletRequest request) {
        ResponseEntity refresh = authService.getRefresh(request);
        return ResponseEntity.ok(refresh.getBody());
    }
}