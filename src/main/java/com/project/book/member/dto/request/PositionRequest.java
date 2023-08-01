package com.project.book.member.dto.request;

import com.project.book.member.domain.MemberType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class PositionRequest {

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private MemberType position;

    @Builder
    public PositionRequest(MemberType position) {
        this.position = position;
    }
}
