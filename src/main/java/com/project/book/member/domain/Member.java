package com.project.book.member.domain;

import com.project.book.common.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Email;

@DynamicUpdate
@DynamicInsert
@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Embedded
    @Column(name = "member_nickname")
    private Nickname nickname;

    @Column(name = "member_O_Auth", unique = true)
    private String oAuth;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type")
    private MemberType type;

    public void updateMemberPosition(final MemberType position) {
        this.type = position;
    }

    public void updateMemberNickname(final Nickname nickname) {
        this.nickname = nickname;
    }
}
