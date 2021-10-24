package com.project.book.dto.kakao;

import com.project.book.dto.kakao.KaKaoBookInfoDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoBookDto {

    private List<KaKaoBookInfoDto> documents;

}
