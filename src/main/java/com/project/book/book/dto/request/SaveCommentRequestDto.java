package com.project.book.book.dto.request;

import com.project.book.book.domain.Comment;
import com.project.book.common.exception.InvalidLengthException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.project.book.book.domain.Comment.MAX_CONTENT_LENGTH;

@Getter
@NoArgsConstructor
public class SaveCommentRequestDto {

    private String content;

    public SaveCommentRequestDto(String content) {
        this.content = content;
    }

    public Comment toComment(Long memberId, Long bookId, String nickname, String oAuth) {
        return Comment.builder()
                .bookId(bookId)
                .memberId(memberId)
                .content(this.content)
                .nickname(nickname)
                .oAuth(oAuth)
                .build();
    }

    public void validate() {
        if (this.content.isBlank() || this.content.length() > MAX_CONTENT_LENGTH) {
            throw new InvalidLengthException();
        }
    }
}
