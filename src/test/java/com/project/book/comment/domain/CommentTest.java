package com.project.book.comment.domain;

import com.project.book.book.domain.Book;
import com.project.book.book.domain.Comment;
import com.project.book.common.exception.InvalidLengthException;
import com.project.book.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


public class CommentTest {

    private Member member;
    private Book book;

    @BeforeEach
    void setUp() {
        member = Member.builder().id(1L).build();
        book = Book.builder().id(1L).build();
    }

    @DisplayName("댓글 길이가 200자 이하면 생성된다.")
    @Test
    void createComment() {
        //given
        String content = "1".repeat(200);

        //when, then
        assertThatNoException().isThrownBy(
                () -> Comment.builder()
                        .bookId(book.getId())
                        .memberId(member.getId())
                        .content(content)
                        .build()
        );
    }

    @DisplayName("댓글 길이가 200자 초과면 생성되지 않는다.")
    @Test
    void failByLength() {
        // given
        String content = "1".repeat(201);

        // when, then
        assertThatThrownBy(
                () -> Comment.builder()
                        .bookId(book.getId())
                        .memberId(member.getId())
                        .content(content)
                        .build()
        ).isInstanceOf(InvalidLengthException.class);
    }

    @DisplayName("공백만 있는 댓글은 생성되지 않는다")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"   "})
    void failBySpace(String content) {
        // when, then
        assertThatThrownBy(
                () -> Comment.builder()
                        .bookId(book.getId())
                        .memberId(member.getId())
                        .content(content)
                        .build()
        ).isInstanceOf(InvalidLengthException.class);
    }

    @DisplayName("문자열이 포함되면, 앞뒤 공백도 카운트한다.")
    @Test
    void failByLength2() {
        // given
        String startSpace = " ";
        String content = "1".repeat(199);
        String endSpace = " ";

        String newContent = startSpace + content + endSpace;

        // when, then
        assertThatThrownBy(
                () -> Comment.builder()
                        .bookId(book.getId())
                        .memberId(member.getId())
                        .content(newContent)
                        .build()
        ).isInstanceOf(InvalidLengthException.class);

        assertThat(newContent.length()).isEqualTo(201);
    }

    @DisplayName("댓글은 수정할 수 있다.")
    @Test
    void editComment() {
        // given
        String originalContent = "123";
        String updatedContent = "321";

        Comment comment = Comment.builder()
                .bookId(book.getId())
                .memberId(member.getId())
                .content(originalContent)
                .build();

        //when
        comment.updateContent(updatedContent);

        //then
        assertAll(
                () -> {
                    assertThat(comment.getContent()).isEqualTo(updatedContent);
                    assertThat(comment.getContent()).isNotEqualTo(originalContent);
                }
        );
    }

    @DisplayName("댓글의 소유자가 맞으면 True를 반환한다.")
    @Test
    void isOwner() {
        // given
        Comment comment = Comment.builder()
                            .content("test")
                            .bookId(book.getId())
                            .memberId(member.getId()).build();

        // when
        boolean isOwner = comment.isOwner(member);

        // then
        assertTrue(isOwner);
    }

    @DisplayName("댓글의 소유자가 아니면 False를 반환한다.")
    @Test
    void FailisOwner() {
        // given
        Comment comment = Comment.builder()
                .content("test")
                .bookId(book.getId())
                .memberId(member.getId()).build();

        Member newMember = Member.builder().id(2L).build();

        // when
        boolean isOwner = comment.isOwner(newMember);

        // then
        assertFalse(isOwner);
    }

}
