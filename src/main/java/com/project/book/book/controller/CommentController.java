package com.project.book.book.controller;

import com.project.book.book.domain.Comment;
import com.project.book.book.dto.request.SaveCommentRequestDto;
import com.project.book.book.service.CommentService;
import com.project.book.common.config.jwt.LoginMember;
import com.project.book.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/book/{bookId}/comments")
    public ResponseEntity<?> getCommentsByBook(
                                                @PathVariable final Long bookId
    ) {
        return commentService.getComments(bookId);
    }

    @PostMapping("/book/{bookId}/comments")
    public ResponseEntity<?> saveComment(
                                            @LoginMember final Member member,
                                            @PathVariable final Long bookId,
                                            @RequestBody @Valid final SaveCommentRequestDto request
    ) {
        Comment comment = commentService.saveComment(member, bookId, request);
        return ResponseEntity.created(
                URI.create("/comments/" + comment.getId()))
                .build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
                                            @LoginMember final Member member,
                                            @PathVariable final Long commentId
    ) {
        return commentService.deleteComment(member, commentId);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(
                                            @LoginMember final Member member,
                                            @PathVariable final Long commentId,
                                            @RequestBody @Valid final SaveCommentRequestDto request
    ) {
        return commentService.updateComment(member, commentId, request);
    }

}

