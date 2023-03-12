package com.project.book.book.repository.impl;

import com.project.book.book.dto.response.CommentResponseDto;
import com.project.book.book.dto.response.QCommentResponseDto;
import com.project.book.book.repository.CommentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.book.book.domain.QComment.comment;
import static com.project.book.common.domain.EntityStatus.*;


@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentResponseDto> findCommentListByBook(Long bookId) {
        return queryFactory.select(new QCommentResponseDto(
                        comment.id, comment.nickname, comment.oAuth,
                        comment.content, comment.createdAt))
                .from(comment)
                .where(
                        comment.bookId.eq(bookId),
                        comment.deleteYn.eq(DeleteYn.N)
                ).fetch();
    }
}