package com.project.book.book.repository;

import com.project.book.book.domain.RegisterBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterBookRepository extends JpaRepository<RegisterBook, Long>, RegisterBookRepositoryCustom {
}
