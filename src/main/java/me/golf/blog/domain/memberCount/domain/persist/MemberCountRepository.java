package me.golf.blog.domain.memberCount.domain.persist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCountRepository extends JpaRepository<MemberCount, Long> {
}