package com.project.book.book.repository;

import com.project.book.book.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentResponseDto> findCommentListByBook(Long bookId);
}
