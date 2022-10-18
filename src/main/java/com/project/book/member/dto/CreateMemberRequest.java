package com.project.book.member.dto;

import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import lombok.Builder;

public class CreateMemberRequest {

    private final String oAuth;

    private final String email;

    private final String nickname;

    private final MemberType type;

    @Builder
    public CreateMemberRequest(String oAuth, String email, String nickname, MemberType type) {
        this.oAuth = oAuth;
        this.email = email;
        this.nickname = nickname;
        this.type = type;
    }

    public Member toMember() {
        return Member.builder()
                .oAuth(oAuth)
                .email(email)
                .nickname(nickname)
                .type(type)
                .build();
    }
}
