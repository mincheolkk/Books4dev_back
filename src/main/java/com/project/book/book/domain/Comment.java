package com.project.book.book.domain;

import com.project.book.common.domain.BaseEntity;
import com.project.book.common.exception.InvalidLengthException;
import com.project.book.member.domain.Member;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@DynamicUpdate
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    public static final int MAX_CONTENT_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = MAX_CONTENT_LENGTH)
    private String content;

    @NotNull
    private Long bookId;

    @NotNull
    private Long memberId;

    @NotNull
    @Column(name = "comment_oAuth")
    private String oAuth;

    @NotNull
    @Column(name = "comment_nickname")
    private String nickname;

    @Builder
    public Comment(Long id, String content, Long bookId, Long memberId, String oAuth, String nickname) {
        validate(content);
        this.id = id;
        this.content = content;
        this.bookId = bookId;
        this.memberId = memberId;
        this.oAuth = oAuth;
        this.nickname = nickname;
    }

    public boolean isOwner(Member member) {
        return this.memberId.equals(member.getId());
    }

    public void updateContent(String content) {
        validate(content);
        this.content = content;
    }

    public void validate(String content) {
        if (content.isBlank() || content.length() > MAX_CONTENT_LENGTH) {
            throw new InvalidLengthException();
        }
    }

}
