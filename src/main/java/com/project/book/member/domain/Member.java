package com.project.book.member.domain;

import com.project.book.book.domain.RegisterBook;
import com.project.book.book.domain.WishMember;
import com.project.book.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "member_nickname")
    private String nickname;

    @Column(name = "member_oAuth", unique = true)
    private String oAuth;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type")
    private MemberType type;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private Set<RegisterBook> registerBooks = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private Set<WishMember> WishBooks = new HashSet<>();

    public void updateMemberPosition(final MemberType position) {
        this.type = position;
    }
}
