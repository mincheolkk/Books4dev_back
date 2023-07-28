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
import com.project.book.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.project.book.common.domain.EntityStatus.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public List<CommentResponseDto> getComments(final Long bookId) {
        return commentRepository.findCommentListByBook(bookId);
    }

    @Transactional
    public Long saveComment(final String oAuth, final Long bookId, final SaveCommentRequestDto request) {
        Member member = memberRepository.findByoAuth(oAuth);

        Comment comment = request.toComment(member.getId(), bookId, member.getNickname().getNickname(), member.getOAuth());
        commentRepository.save(comment);
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new ContentNotFoundException());

        book.calculateCommentCount(1);
        return comment.getId();
    }

    @Transactional
    public void deleteComment(final String oAuth, final Long commentId) {
        Member member = memberRepository.findByoAuth(oAuth);

        Comment comment = validateOwner(member, commentId);
        comment.changeDeleteYn(DeleteYn.Y);

        Long bookId = comment.getBookId();
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new ContentNotFoundException());
        book.calculateCommentCount(-1);
    }

    @Transactional
    public void updateComment(final String oAuth, final Long commentId,
                                           final SaveCommentRequestDto request) {
        Member member = memberRepository.findByoAuth(oAuth);

        Comment comment = validateOwner(member, commentId);
        comment.updateContent(request.getContent());
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
