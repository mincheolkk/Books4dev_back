package com.project.book.book.repository.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WishBookRepositoryImpl {

    private final JPAQueryFactory queryFactory;

}
