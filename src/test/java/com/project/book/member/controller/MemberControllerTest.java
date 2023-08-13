package com.project.book.member.controller;

import com.project.book.book.domain.BookTime;
import com.project.book.book.dto.response.ReadBookResponseDto;
import com.project.book.book.dto.response.WishBookResponseDto;
import com.project.book.common.exception.ExistNicknameException;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import com.project.book.member.domain.Nickname;
import com.project.book.member.dto.request.NicknameRequest;
import com.project.book.member.dto.request.PositionRequest;
import com.project.book.member.dto.response.MemberResponse;
import com.project.book.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.project.book.book.domain.BookTime.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTest {

    private static final long MEMBER_ID = 1L;

    @DisplayName("유저의 직군을 조회한다.")
    @Test
    void checkPosition() throws Exception {
        // given
        String oAuth = "123";
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);
        given(memberService.hasPosition(anyString())).willReturn(MemberType.BackEnd);

        // when & then
        mockMvc.perform(
                    get("/member/checkPosition"))
                .andExpect(status().isOk());
    }

    @DisplayName("유저의 직군을 조회한다. 조회결과가 null이다.")
    @Test
    void checkPositionIsNull() throws Exception {
        // given
        String oAuth = "123";
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);
        given(memberService.hasPosition(anyString())).willReturn(null);

        // when & then
        mockMvc.perform(
                        get("/member/checkPosition"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("직군을 추가한다. 요청이 null인 경우 예외가 발생한다")
    @Test
    void addPositionButFail() throws Exception {
        // given
        String oAuth = "123";
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);

        PositionRequest request = new PositionRequest();
        willDoNothing().given(memberService).addPosition(anyString(), any());


        // when & then
        mockMvc.perform(
                        post("/member/selectPosition")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("must not be null"));
    }

    @DisplayName("직군을 추가한다.")
    @Test
    void selectPosition() throws Exception {
        // given
        String oAuth = "123";
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);

        PositionRequest request = PositionRequest.builder()
                .position(MemberType.BackEnd)
                .build();

        willDoNothing().given(memberService).addPosition(anyString(), any());

        // when & then
        mockMvc.perform(
                        post("/member/selectPosition")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("유저의 <관심있는 책> 리스트를 조회한다.")
    @Test
    void getMemberWishBook() throws Exception {
        // given
        List<WishBookResponseDto> wishBooks = createWishBooks();
        given(wishBookService.getMemberWishBook(anyLong())).willReturn(wishBooks);

        // when & then
        mockMvc.perform(get("/member/" + MEMBER_ID + "/wishBook")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("ProGit"))
                .andExpect(jsonPath("$[1].title").value("Effective Java"))
                .andExpect(jsonPath("$.length()").value(wishBooks.size()));
    }

    private List<WishBookResponseDto> createWishBooks() {
        return List.of(
                        WishBookResponseDto.builder()
                                .title("ProGit")
                                .build(),
                        WishBookResponseDto.builder()
                                .title("Effective Java")
                                .build()
        );
    }

    @DisplayName("유저의 <읽은 책> 맵을 조회한다.")
    @Test
    void getMemberReadBook() throws Exception {
        // given
        Map<BookTime, List<ReadBookResponseDto>> readBookMap = createReadBookMap();
        given(readBookService.getMemberReadBook(any())).willReturn(readBookMap);

        // when & then
        mockMvc.perform(
                        get("/member/" + MEMBER_ID + "/readBook")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.after[0].title").value("MySQL 8.0"))
                .andExpect(jsonPath("$.after[0].star").value(4.5))
                .andExpect(jsonPath("$.after.length()").value(readBookMap.get(after).size()))
                .andExpect(jsonPath("$.threeYear[0].title").value("CleanCode"))
                .andExpect(jsonPath("$.threeYear[0].star").value(5.0))
                .andExpect(jsonPath("$.threeYear.length()").value(readBookMap.get(threeYear).size()));
    }

    private Map<BookTime, List<ReadBookResponseDto>> createReadBookMap() {
        Map<BookTime, List<ReadBookResponseDto>> readBooks = new HashMap<>();

        List<ReadBookResponseDto> readTimeIsAfterBooks = List.of(
                ReadBookResponseDto.builder()
                        .title("MySQL 8.0")
                        .star(4.5)
                        .build()
        );
        readBooks.put(after, readTimeIsAfterBooks);

        List<ReadBookResponseDto> readTimeIsThreeYearsBooks = List.of(
                ReadBookResponseDto.builder()
                        .title("CleanCode")
                        .star(5.0)
                        .build()
        );
        readBooks.put(threeYear, readTimeIsThreeYearsBooks);

        return readBooks;
    }

    @DisplayName("닉네임을 추가하거나 변경한다.")
    @Test
    void addNickname() throws Exception {
        // given
        String oAuth = "123";
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);

        String testNickname = "testNickname";
        NicknameRequest request = new NicknameRequest(testNickname);

        // when & then
        mockMvc.perform(
                    post("/member/nickname")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @DisplayName("이미 존재하는 닉네임으로 요청할 경우에는 예외가 발생한다.")
    @Test
    void addNicknameButFail() throws Exception {
        // given
        String oAuth = "123";
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);

        String testNickname = "testNickname";
        NicknameRequest request = new NicknameRequest(testNickname);

        willThrow(ExistNicknameException.class)
                .given(memberService).addNickname(anyString(), any(NicknameRequest.class));

        // when & then
        mockMvc.perform(
                    post("/member/nickname")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotAcceptable())
                .andExpect(status().is(406));
    }

    @DisplayName("유저의 프로필을 조회한다.")
    @Test
    void getMemberProfile() throws Exception {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .nickname(new Nickname("test"))
                .build();
        MemberResponse memberResponse = MemberResponse.from(member);

        given(memberService.getMemberProfile(any())).willReturn(memberResponse);

        // when & then
        mockMvc.perform(
                        get("/member/" + MEMBER_ID)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.oauth").value(member.getOAuth()))
                .andExpect(jsonPath("$.nickname").value(member.getNickname().getNickname()));
    }
}
