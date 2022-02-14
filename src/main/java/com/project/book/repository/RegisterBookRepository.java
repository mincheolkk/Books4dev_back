package com.project.book.repository;

import com.project.book.domain.RegisterBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterBookRepository extends JpaRepository<RegisterBook, Long>, RegisterBookRepositoryCustom {
}
