package com.project.book.member.controller;

import com.project.book.common.config.jwt.LoginMember;
 import com.project.book.member.dto.LoginMemberDto;
import com.project.book.member.dto.request.TokenRequest;
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
    public ResponseEntity<MemberResponse> getMyProfile(@LoginMember final LoginMemberDto loginMemberDto) {
        return authService.getMyProfile(loginMemberDto.getOAuth());
    }

    @PostMapping(value = "/update/token")
    public ResponseEntity<?> updateAccessToken(@LoginMember final LoginMemberDto loginMemberDto, @RequestBody @Valid final TokenRequest tokenRequest) {
        String newAccessToken = authService.updateAccessToken(loginMemberDto.getOAuth(), tokenRequest);
        return ResponseEntity.ok(newAccessToken);
    }

    @GetMapping(value = "/out")
    public ResponseEntity<?> logOut(final HttpServletRequest request, @LoginMember final LoginMemberDto loginMemberDto) {
        authService.logOut(request, loginMemberDto.getOAuth());
        return ResponseEntity.noContent().build();
    }


    @GetMapping(value = "/refreshtoken")
    public ResponseEntity<?> getRefreshToken(@LoginMember final LoginMemberDto loginMemberDto) {
        ResponseEntity refresh = authService.createRefreshToken(loginMemberDto.getOAuth());
        return ResponseEntity.ok(refresh.getBody());
    }
}