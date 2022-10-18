package com.project.book.book.repository;

import com.project.book.book.domain.WishMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishMemberRepository extends JpaRepository<WishMember, Long>, WishMemberRepositoryCustom {
}
