package com.project.book.member.dto;

import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {
    private final Long id;
    private final String oAuth;
    private final String email;
    private final String nickname;
    private final MemberType type;

    @Builder
    public MemberResponse(Long id, String oAuth, String email, String nickname, MemberType type) {
        this.id = id;
        this.oAuth = oAuth;
        this.email = email;
        this.nickname = nickname;
        this.type = type;
    }

    public static MemberResponse from(final Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .oAuth(member.getOAuth())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .type(member.getType())
                .build();
    }

}
