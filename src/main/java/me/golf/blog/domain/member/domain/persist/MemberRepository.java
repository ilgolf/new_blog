package me.golf.blog.domain.member.domain.persist;

import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Nickname;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    boolean existsByEmail(final Email email);
    boolean existsByNickname(final Nickname nickname);
}