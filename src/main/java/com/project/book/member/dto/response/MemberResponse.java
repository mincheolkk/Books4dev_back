package com.project.book.member.dto.response;

import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class MemberResponse {

    private String oAuth;
    private MemberType memberType;

    public static MemberResponse from(final Member member) {
        return MemberResponse.builder()
                .oAuth(member.getOAuth())
                .memberType(member.getType())
                .build();
    }


}
