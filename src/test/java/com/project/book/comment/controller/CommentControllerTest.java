package com.project.book.comment.controller;

import com.project.book.book.dto.request.SaveCommentRequestDto;
import com.project.book.book.dto.response.CommentResponseDto;
import com.project.book.common.exception.ContentNotFoundException;
import com.project.book.common.exception.InvalidOwnerException;
import com.project.book.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommentControllerTest extends ControllerTest {

    private static final long BOOK_ID = 1L;
    private static final long COMMENT_ID = 1L;

    @DisplayName("책에 추가된 댓글을 리스트로 조회한다.")
    @Test
    void getCommentsByBook() throws Exception {
        // given
        List<CommentResponseDto> comments = createComment();
        given(commentService.getComments(anyLong())).willReturn(comments);

        // when & then
        mockMvc.perform(
                    get("/book/" + BOOK_ID + "/comments")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nickname").value("tester"))
                .andExpect(jsonPath("$[0].content").value("책 좋아요"))
                .andExpect(jsonPath("$[1].nickname").value("tester2"))
                .andExpect(jsonPath("$[1].content").value("추천해요"))
                .andExpect(jsonPath("$.length()").value(comments.size()));
    }

    private List<CommentResponseDto> createComment() {
        return List.of(
                CommentResponseDto.builder()
                        .oAuth("123")
                        .nickname("tester")
                        .content("책 좋아요")
                        .build(),
                CommentResponseDto.builder()
                        .oAuth("456")
                        .nickname("tester2")
                        .content("추천해요")
                        .build()
        );
    }

    @DisplayName("댓글을 생성한다.")
    @Test
    void saveComment() throws Exception {
        // given
        String oAuth = "123";
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);

        String testComment = "This is Comment";
        SaveCommentRequestDto request = new SaveCommentRequestDto(testComment);

        given(commentService.saveComment(anyString(), anyLong(), any(SaveCommentRequestDto.class)))
                .willReturn(COMMENT_ID);

        // when & then
        mockMvc.perform(
                        post("/book/" + BOOK_ID + "/comments")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/comments/" + COMMENT_ID));
    }

    @DisplayName("댓글을 삭제한다.")
    @Test
    void deleteComment() throws Exception {
        // given
        String oAuth = "123";
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);

        // when & then
        mockMvc.perform(
                        delete("/comments/" + COMMENT_ID)
                )
                .andExpect(status().isNoContent())
                .andExpect(status().is(204));
    }

    @DisplayName("삭제 실패. 본인 댓글이 아닌 경우에는 예외가 발생한다.")
    @Test
    void deleteButInvalidOwner() throws Exception {
        // given
        String oAuth = "123";
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);

        willThrow(InvalidOwnerException.class)
                .given(commentService).deleteComment(anyString(),anyLong());

        // when & then
        mockMvc.perform(
                        delete("/comments/" + COMMENT_ID)
                )
                .andExpect(status().isNotAcceptable())
                .andExpect(status().is(406));
    }

    @DisplayName("삭제 실패. 댓글이나 책을 찾을 수 없는 경우에는 예외가 발생한다.")
    @Test
    void deleteButContentNotFound() throws Exception {
        // given
        String oAuth = "123";
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);


        willThrow(ContentNotFoundException.class)
                .given(commentService).deleteComment(anyString(),anyLong());

        // when & then
        mockMvc.perform(
                        delete("/comments/" + COMMENT_ID)
                )
                .andExpect(status().isNotFound())
                .andExpect(status().is(404));
    }

    @DisplayName("댓글을 수정한다.")
    @Test
    void updateComment() throws Exception {
        // given
        String oAuth = "123";
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);

        String testComment = "Updated Comment";
        SaveCommentRequestDto request = new SaveCommentRequestDto(testComment);

        // when & then
        mockMvc.perform(
                        patch("/comments/" + COMMENT_ID)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @DisplayName("수정 실패. 본인 댓글이 아닌 경우에는 예외가 발생한다.")
    @Test
    void updateButInvalidOwner() throws Exception {
        // given
        String oAuth = "123";
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);

        String testComment = "Updated Comment";
        SaveCommentRequestDto request = new SaveCommentRequestDto(testComment);

        willThrow(InvalidOwnerException.class)
                .given(commentService).updateComment(anyString(),anyLong(), any(SaveCommentRequestDto.class));

        // when & then
        mockMvc.perform(
                        patch("/comments/" + COMMENT_ID)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotAcceptable())
                .andExpect(status().is(406));
    }

    @DisplayName("수정 실패. 댓글을 찾을 수 없는 경우에는 예외가 발생한다.")
    @Test
    void updateButContentNotFound() throws Exception {
        // given
        String oAuth = "123";
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);

        String testComment = "Updated Comment";
        SaveCommentRequestDto request = new SaveCommentRequestDto(testComment);

        willThrow(ContentNotFoundException.class)
                .given(commentService).updateComment(anyString(),anyLong(), any(SaveCommentRequestDto.class));

        // when & then
        mockMvc.perform(
                        patch("/comments/" + COMMENT_ID)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(status().is(404));
    }
}
