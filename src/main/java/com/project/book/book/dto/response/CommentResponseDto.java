package com.project.book.book.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class CommentResponseDto {

    private Long id;
    private String nickname;
    private String oAuth;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public CommentResponseDto(Long id, String nickname, String oAuth, String content, LocalDateTime createdAt) {
        this.id = id;
        this.nickname = nickname;
        this.oAuth = oAuth;
        this.content = content;
        this.createdAt = createdAt;
    }
}
