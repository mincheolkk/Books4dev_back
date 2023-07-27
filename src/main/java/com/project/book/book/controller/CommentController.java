package com.project.book.book.controller;

import com.project.book.book.dto.request.SaveCommentRequestDto;
import com.project.book.book.dto.response.CommentResponseDto;
import com.project.book.book.service.CommentService;
import com.project.book.common.config.jwt.LoginMember;
import com.project.book.member.dto.LoginMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/book/{bookId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByBook(
                                                @PathVariable final Long bookId
    ) {
        List<CommentResponseDto> comments = commentService.getComments(bookId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/book/{bookId}/comments")
    public ResponseEntity<Void> saveComment(
                                            @LoginMember final LoginMemberDto loginMemberDto,
                                            @PathVariable final Long bookId,
                                            @RequestBody @Valid final SaveCommentRequestDto request
    ) {
        request.validate();
        Long commentId = commentService.saveComment(loginMemberDto.getOAuth(), bookId, request);
        return ResponseEntity.created(
                URI.create("/comments/" + commentId))
                .build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
                                            @LoginMember final LoginMemberDto loginMemberDto,
                                            @PathVariable final Long commentId
    ) {
        commentService.deleteComment(loginMemberDto.getOAuth(), commentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
                                            @LoginMember final LoginMemberDto loginMemberDto,
                                            @PathVariable final Long commentId,
                                            @RequestBody @Valid final SaveCommentRequestDto request
    ) {
        commentService.updateComment(loginMemberDto.getOAuth(), commentId, request);
        return ResponseEntity.ok().build();
    }

}

