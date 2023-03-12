package com.project.book.book.domain;

import com.project.book.common.domain.BaseEntity;
import com.project.book.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    public static final int MIN_CONTENT_LENGTH = 1;
    public static final int MAX_CONTENT_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = MAX_CONTENT_LENGTH)
    private String content;

    private Long bookId;
    private Long memberId;

    @Column(name = "comment_oAuth")
    private String oAuth;

    @Column(name = "comment_nickname")
    private String nickname;

    @Builder
    public Comment(Long id, String content, Long bookId, Long memberId, String oAuth, String nickname) {
        this.id = id;
        this.content = content;
        this.bookId = bookId;
        this.memberId = memberId;
        this.oAuth = oAuth;
        this.nickname = nickname;
    }

    public boolean isOwner(Member member) {
        return this.memberId == member.getId();
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
