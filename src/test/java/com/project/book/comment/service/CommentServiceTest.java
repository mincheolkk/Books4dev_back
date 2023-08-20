package com.project.book.comment.service;

import com.project.book.book.domain.Book;
import com.project.book.book.domain.Comment;
import com.project.book.book.dto.request.SaveCommentRequestDto;
import com.project.book.book.dto.response.CommentResponseDto;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.repository.CommentRepository;
import com.project.book.book.service.CommentService;
import com.project.book.common.exception.ContentNotFoundException;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.Nickname;
import com.project.book.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static com.project.book.common.domain.EntityStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentService commentService;

    private Member member;
    private Book book;

    @BeforeEach
    void setUp() {
        String nickname = "mincheol";
        member = Member.builder()
                .nickname(new Nickname(nickname))
                .oAuth("123")
                .build();
        memberRepository.save(member);

        book = Book.builder().build();
        bookRepository.save(book);
    }

    @DisplayName("댓글을 생성한다.")
    @Test
    void saveComment() {
        // given
        String text = "테스트입니다.";
        SaveCommentRequestDto saveCommentRequestDto = new SaveCommentRequestDto(text);

        // when
        Long commentId = commentService.saveComment(member.getOAuth(), book.getId(), saveCommentRequestDto);

        // then
        Comment savedComment = commentRepository.findById(commentId).get();
        assertAll(
                () -> {
                    assertThat(savedComment.getId()).isEqualTo(commentId);
                    assertThat(savedComment.getContent()).isEqualTo(text);
                    assertThat(savedComment.getOAuth()).isEqualTo(member.getOAuth());
                    assertThat(savedComment.getBookId()).isEqualTo(book.getId());
                }
        );
    }

    @DisplayName("댓글을 생성하면, 책의 댓글 카운트를 올린다.")
    @Test
    void plusCommentCount() {
        // given
        String text = "테스트입니다.";
        SaveCommentRequestDto saveCommentRequestDto = new SaveCommentRequestDto(text);

        // when
        commentService.saveComment(member.getOAuth(), book.getId(), saveCommentRequestDto);

        // then
        Book savedBook = bookRepository.findById(book.getId()).get();
        assertThat(savedBook.getCount().getCommentCount()).isEqualTo(1);
    }

    @DisplayName("책이 존재하지 않는 경우, 댓글은 생성되지 않고 예외를 발생한다")
    @Test
    void saveCommentButContentNotFound() {
        // given
        String text = "테스트입니다.";
        SaveCommentRequestDto saveCommentRequestDto = new SaveCommentRequestDto(text);
        Long fakeBookId = 10L;

        // when & then
        assertThatThrownBy(() -> commentService.saveComment(member.getOAuth(), fakeBookId, saveCommentRequestDto))
                .isInstanceOf(ContentNotFoundException.class)
                .hasMessage("찾을 수 없습니다.");
    }

    @DisplayName("댓글을 조회한다.")
    @Test
    void getComments() {
        // given
        String text = "테스트입니다.";
        SaveCommentRequestDto saveCommentRequestDto = new SaveCommentRequestDto(text);
        commentService.saveComment(member.getOAuth(), book.getId(), saveCommentRequestDto);


        // when
        List<CommentResponseDto> comments = commentService.getComments(book.getId());

        // then
        assertAll(
                () -> {
                    assertThat(comments.size()).isEqualTo(1);
                    assertThat(comments.get(0).getContent()).isEqualTo(text);
                }
        );
    }

    @DisplayName("댓글을 삭제하면, 객체의 DeleteYn이 Y로 바뀐다.")
    @Test
    void deleteComment() {
        // given
        String text = "테스트입니다.";
        SaveCommentRequestDto saveCommentRequestDto = new SaveCommentRequestDto(text);

        Long commentId = commentService.saveComment(member.getOAuth(), book.getId(), saveCommentRequestDto);

        // when
        commentService.deleteComment(member.getOAuth(), commentId);

        // then
        Comment savedComment = commentRepository.findById(commentId).get();
        assertThat(savedComment.getDeleteYn()).isEqualTo(DeleteYn.Y);
    }

    @DisplayName("댓글을 삭제하면, 책의 댓글 카운트를 내린다.")
    @Test
    void minusCommentCount() {
        // given
        String text = "테스트입니다.";
        SaveCommentRequestDto saveCommentRequestDto = new SaveCommentRequestDto(text);

        Long commentId = commentService.saveComment(member.getOAuth(), book.getId(), saveCommentRequestDto);

        // when
        commentService.deleteComment(member.getOAuth(), commentId);

        // then
        Book savedBook = bookRepository.findById(book.getId()).get();
        assertThat(savedBook.getCount().getCommentCount()).isEqualTo(0);
    }

    @DisplayName("댓글을 수정한다.")
    @Test
    void updateComment() {
        // given
        String text = "테스트입니다.";
        SaveCommentRequestDto saveCommentRequestDto = new SaveCommentRequestDto(text);

        Long commentId = commentService.saveComment(member.getOAuth(), book.getId(), saveCommentRequestDto);


        // when
        String text2 = "수정한다.";
        SaveCommentRequestDto saveCommentRequestDto2 = new SaveCommentRequestDto(text2);

        commentService.updateComment(member.getOAuth(), commentId, saveCommentRequestDto2);

        // then
        Comment savedComment = commentRepository.findById(commentId).get();
        assertAll(
                () -> {
                    assertThat(savedComment.getContent()).isNotEqualTo(text);
                    assertThat(savedComment.getContent()).isEqualTo(text2);
                }
        );
    }

    @DisplayName("댓글을 수정해도, 책의 댓글 카운트는 바뀌지 않는다.")
    @Test
    void noChangeCommentCount() {
        // given
        String text = "테스트입니다.";
        SaveCommentRequestDto saveCommentRequestDto = new SaveCommentRequestDto(text);

        Long commentId = commentService.saveComment(member.getOAuth(), book.getId(), saveCommentRequestDto);


        // when
        String text2 = "수정한다.";
        SaveCommentRequestDto saveCommentRequestDto2 = new SaveCommentRequestDto(text2);

        commentService.updateComment(member.getOAuth(), commentId, saveCommentRequestDto2);

        // then
        Book savedBook = bookRepository.findById(book.getId()).get();
        assertThat(savedBook.getCount().getCommentCount()).isEqualTo(1);
    }
}
