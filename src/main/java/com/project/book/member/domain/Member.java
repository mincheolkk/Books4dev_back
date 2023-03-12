package com.project.book.member.domain;

import com.project.book.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_nickname")
    private String nickname;

    @Column(name = "member_oAuth", unique = true)
    private String oAuth;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type")
    private MemberType type;

    public void updateMemberPosition(final MemberType position) {
        this.type = position;
    }

    public void updateMemberNickname(final String nickname) {
        this.nickname = nickname;
    }
}
