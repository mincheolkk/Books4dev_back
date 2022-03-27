package com.project.book.repository.member;

import com.project.book.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByoAuth(String oAuth);

    boolean existsByoAuth(String oAuth);
}
