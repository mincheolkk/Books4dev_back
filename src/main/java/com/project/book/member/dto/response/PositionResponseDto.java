package com.project.book.member.dto.response;

import com.project.book.member.domain.MemberType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@Getter
public class PositionResponseDto {

    @Enumerated(value = EnumType.STRING)
    private MemberType position;
}
