package com.project.book.service;

import com.project.book.domain.Book;
import com.project.book.dto.kakao.KaKaoBookInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Service
public class BookService {

    public void createBook(@RequestBody @Valid KaKaoBookInfoDto request) {

    }
}
