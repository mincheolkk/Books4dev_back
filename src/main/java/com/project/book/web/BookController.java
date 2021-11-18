package com.project.book.web;


import com.project.book.dto.kakao.KaKaoBookInfoDto;
import com.project.book.service.BookService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // 책 저장 로직
    @PostMapping()
    public void saveBook(KaKaoBookInfoDto request) {
        // isbn 으로 검색해서 디비에 없으면 추가

        String isbn = request.getIsbn();
        request.getAuthors();


    }
}
