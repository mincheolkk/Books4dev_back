package com.project.book.book.dto.request;

import com.project.book.book.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveCommentRequestDto {

    private String content;

    public Comment toComment(Long memberId, Long bookId, String nickname, String oAuth) {
        return Comment.builder()
                .bookId(bookId)
                .memberId(memberId)
                .content(this.content)
                .nickname(nickname)
                .oAuth(oAuth)
                .build();
    }
}
