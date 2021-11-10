package com.project.book.repository.service;

import com.project.book.repository.BookRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

}
