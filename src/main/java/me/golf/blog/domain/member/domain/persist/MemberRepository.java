package me.golf.blog.domain.member.domain.persist;

import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.dto.MemberResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    boolean existsByEmail(final Email email);
    boolean existsByNickname(final Nickname nickname);
    Optional<Member> findByNickname(final Nickname nickname);
}