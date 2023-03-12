package com.project.book.book.repository;

import com.project.book.book.domain.ReadBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadBookRepository extends JpaRepository<ReadBook, Long>, ReadBookRepositoryCustom {
}
