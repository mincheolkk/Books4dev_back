package com.project.book.service;

import com.project.book.domain.Book;
import com.project.book.dto.kakao.KaKaoBookInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Service
public class BookService {

    // db 쪼개기
    // MemberBook -> registerBook
    // commentBook

    public void createBook(@RequestBody @Valid KaKaoBookInfoDto request) {
        // request.toEntity 쓸지 말지. 다른 사람들은 dto 에서 엔티티로
        // dto::of

        // 상세 페이지 들어갈 항목
        // 책 제목, 작가, 번역가, 출판사, 가격 (책 자채 정보)
        // 등록한 사람들이 읽은 시기, 추천 시기, 평점 (책 등록 정보)
        // 등록 기수
        // 일부 통계
        // 댓글 (상세페이지에서 작성)
        // 알라딘 평점, 교보문고 평점 등 (외부 정보)

        // 전체 조회 들어갈 항목
        // 책 제목, 작가, 번역가, 출판사, 가격
        // 등록한 사람들이 읽은 시기, 추천 시기, 평점 (책 등록 정보)
        // 댓글 갯수
        // 하나의 책에 한 명이 여러 댓글을 쓸 수 있다. 그렇기에 책과 댓글의 관계도 생각.


        // 책 등록할 때, 이미 등록된 책이면 등록된 책 id 부여하는 로직


    }
}
