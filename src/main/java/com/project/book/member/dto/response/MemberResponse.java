package com.project.book.member.dto.response;

import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import com.project.book.member.domain.Nickname;
import lombok.*;

@NoArgsConstructor
@Getter
public class MemberResponse {

    private String oAuth;
    private MemberType memberType;
    private String nickname;

    @Builder
    public MemberResponse(String oAuth, MemberType memberType, String nickname) {
        this.oAuth = oAuth;
        this.memberType = memberType;
        this.nickname = nickname;
    }

    public static MemberResponse from(final Member member) {
        return MemberResponse.builder()
                .oAuth(member.getOAuth())
                .memberType(member.getType())
                .nickname(member.getNickname().getNickname())
                .build();
    }
}
