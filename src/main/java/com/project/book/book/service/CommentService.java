package com.project.book.book.service;

import com.project.book.book.domain.Book;
import com.project.book.book.domain.Comment;
import com.project.book.book.dto.request.SaveCommentRequestDto;
import com.project.book.book.dto.response.CommentResponseDto;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.repository.CommentRepository;
import com.project.book.common.exception.ContentNotFoundException;
import com.project.book.common.exception.InvalidOwnerException;
import com.project.book.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.project.book.common.domain.EntityStatus.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    public ResponseEntity<?> getComments(final Long bookId) {
        List<CommentResponseDto> commentList = commentRepository.findCommentListByBook(bookId);

        return new ResponseEntity<>(commentList, HttpStatus.ACCEPTED);
    }

    @Transactional
    public Long saveComment(final Member member, final Long bookId, final SaveCommentRequestDto request) {
        Comment comment = request.toComment(member.getId(), bookId, member.getNickname().getNickname(), member.getOAuth());
        commentRepository.save(comment);

        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new ContentNotFoundException());

        book.calculateCommentCount(1);
        return comment.getId();
    }

    @Transactional
    public ResponseEntity<?> deleteComment(final Member member, final Long commentId) {
        Comment comment = validateOwner(member, commentId);
        comment.changeDeleteYn(DeleteYn.Y);

        Long bookId = comment.getBookId();
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new ContentNotFoundException());
        book.calculateCommentCount(-1);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Transactional
    public ResponseEntity<?> updateComment(final Member member, final Long commentId,
                                           final SaveCommentRequestDto request) {
        Comment comment = validateOwner(member, commentId);

        comment.updateContent(request.getContent());

        return new ResponseEntity(HttpStatus.OK);
    }

    private Comment validateOwner(final Member member, final Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ContentNotFoundException());

        if (!comment.isOwner(member)) {
            throw new InvalidOwnerException();
        }

        return comment;
    }
}
