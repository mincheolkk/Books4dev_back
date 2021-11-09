package com.project.book.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotNull
    @Column(name = "member_name")
    private String name;

    @Positive
    @Column(name = "member_number")
    private Integer number;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type")
    private MemberType type;

    @OneToMany(mappedBy = "member")
    private List<RegisterBook> registerBooks = new ArrayList<>();

}
