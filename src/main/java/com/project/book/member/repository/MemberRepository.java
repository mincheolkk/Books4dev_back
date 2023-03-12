package com.project.book.member.repository;

import com.project.book.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByoAuth(final String oAuth);

    boolean existsByoAuth(final String oAuth);

    boolean existsByNickname(final String nickname);
}
