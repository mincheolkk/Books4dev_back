package com.project.book.member.dto.response;

import com.project.book.member.domain.MemberType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PositionResponseDto {

    private MemberType position;
}
