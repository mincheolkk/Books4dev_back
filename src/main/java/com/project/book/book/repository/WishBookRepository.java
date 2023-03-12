package com.project.book.book.repository;

import com.project.book.book.domain.WishBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishBookRepository extends JpaRepository<WishBook, Long>, WishBookRepositoryCustom {
}
