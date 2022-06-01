package me.golf.blog.domain.member.domain.persist;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "memberCount")
    Optional<Member> findById(final Long id);

    @Modifying
    @Query("update Member m set m.activated = false where m.id = :id")
    void updateActivatedById(@Param("id") Long id);
}