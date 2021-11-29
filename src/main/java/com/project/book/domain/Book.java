package com.project.book.domain;

import lombok.*;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "book_isbn")
    private String isbn;

    @Column(name = "book_title")
    private String title;

    @Column(name = "book_publisher")
    private String publisher;

    @Column(name = "book_dateTime")
    private LocalDateTime dateTime;

    @Column(name = "book_price")
    private int price;

    @Column(name = "book_thumbnail")
    private String thumbnail;

    @Column(name = "book_authors")
    private String authors;

    @Column(name = "book_translator")
    private String translator;

    @OneToMany(mappedBy = "book")
    private List<RegisterBook> registerBooks = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<CommentBook> commentBooks = new ArrayList<>();
}
