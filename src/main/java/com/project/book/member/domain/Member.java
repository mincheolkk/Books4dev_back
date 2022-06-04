package com.project.book.member.domain;

import com.project.book.book.domain.CommentBook;
import com.project.book.book.domain.RegisterBook;
import com.project.book.book.domain.SavedBook;
import com.project.book.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String kakaoId;

    @NotEmpty
    @Column(name = "member_nickname")
    private String nickname;

    @Column(name = "member_oAuth", unique = true)
    private String oAuth;

    @Email
//    @NotEmpty
//    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type")
    private MemberType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_language")
    private Language language;

    @Column(name = "member_refreshToken")
    private String refreshToken;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<RegisterBook> registerBooks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "book")
    private List<CommentBook> commentBooks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "book")
    private List<SavedBook> savedBooks = new ArrayList<>();

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }
}
