package com.project.book.book.controller;

import com.project.book.book.dto.request.*;
import com.project.book.book.dto.response.BookResponseDto;
import com.project.book.book.dto.response.DetailBookResponseDto;
import com.project.book.book.dto.response.KeywordScoreResponseDto;
import com.project.book.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookControllerTest extends ControllerTest {

    private static final long BOOK_ID = 1L;

    @DisplayName("로그인한 유저가 <읽은 책>을 등록한다")
    @Test
    void saveBook() throws Exception {
        // given
        String oAuth = "123";
        SaveBookFromSearchDto request = SaveBookFromSearchDto.builder()
                .info(new BookInfoDto())
                .review(new BookReviewDto())
                .build();

        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);
        given(bookService.saveBookFromKakao(any(), any(SaveBookFromSearchDto.class))).willReturn(BOOK_ID);

        // when & then
        mockMvc.perform(
                        post("/book/fromSearch")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/book/" + BOOK_ID));
    }

    @DisplayName("로그인하지 않은 유저가 <읽은 책>을 등록하면 예외가 발생한다.")
    @Test
    void saveBookButFail() throws Exception {
        // given
        SaveBookFromSearchDto request = SaveBookFromSearchDto.builder()
                .info(new BookInfoDto())
                .review(new BookReviewDto())
                .build();

        // when & then
        mockMvc.perform(
                        post("/book/fromSearch")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("유효하지 않은 Access 토큰입니다."));
    }

    @DisplayName("책 리스트 전체를 페이징 조회한다.")
    @Test
    void getAllBook() throws Exception {
        // given
        CustomPageRequest pageRequest = new CustomPageRequest();
        Long totalCount = 11L;
        List<BookResponseDto> responseDtos = createBookResponseDtos(totalCount);

        Page<BookResponseDto> mockPage = new PageImpl<>(
                responseDtos, pageRequest.toPageable(), totalCount
        );

        given(bookService.getAllBook(any(), any())).willReturn(mockPage);

        // when & then
        mockMvc.perform(
                    get("/book/all")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.offset").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(11))
                .andExpect(jsonPath("$.totalPages").value(2));
    }

    @DisplayName("책 한권을 조회한다.")
    @Test
    void getDetailBook() throws Exception {
        // given
        String title = "testTitle";
        String isbn = "testIsbn";

        DetailBookResponseDto responseDto = DetailBookResponseDto.builder()
                .title(title)
                .isbn(isbn).build();

        given(bookService.getDetailBook(any())).willReturn(responseDto);

        // when & then
        mockMvc.perform(
                        get("/book/" + BOOK_ID)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.isbn").value(isbn));
    }

    @DisplayName("인기 검색어 리스트를 조회한다.")
    @Test
    void getPopularKeyword() throws Exception {
        // given
        String keyword = "java";
        int score = 10;
        List<KeywordScoreResponseDto> popularKeyword = List.of(
                new KeywordScoreResponseDto(keyword, score)
        );

        given(rankingService.getPopularKeyword()).willReturn(popularKeyword);

        // when & then
        mockMvc.perform(
                    get("/book/popular")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].keyword").value(keyword))
                .andExpect(jsonPath("$[0].score").value(score));
    }

    private List<BookResponseDto> createBookResponseDtos(Long num) {
        List<BookResponseDto> responseDtos = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            responseDtos.add(new BookResponseDto());
        }
        return responseDtos;
    }
}
