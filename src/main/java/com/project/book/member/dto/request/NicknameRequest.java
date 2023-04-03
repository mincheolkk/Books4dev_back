package com.project.book.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class NicknameRequest {

    private String nickname;

    public NicknameRequest(String nickname) {
        this.nickname = nickname;
    }

}
