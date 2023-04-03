package com.project.book.member.dto.request;

import com.project.book.member.domain.MemberType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@Getter
public class PositionRequest {

    @Enumerated(value = EnumType.STRING)
    private MemberType position;
}
