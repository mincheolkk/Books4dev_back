package com.project.book.domain;

import lombok.*;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @ISBN
    @NotNull
    @Column(name = "book_isbn")
    private String isbn;

    @NotNull
    @Column(name = "book_title")
    private String title;

    @NotNull
    @Column(name = "book_publisher")
    private String publisher;

    @NotNull
    @Column(name = "book_dateTime")
    private ZonedDateTime dateTime;

    @NotNull
    @Column(name = "book_price")
    private Long price;

    @URL
    @NotNull
    @Column(name = "book_thumbnail")
    private String thumbnail;

    @OneToMany(mappedBy = "book")
    private List<RegisterBook> registerBooks = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<CommentBook> commentBooks = new ArrayList<>();
}
