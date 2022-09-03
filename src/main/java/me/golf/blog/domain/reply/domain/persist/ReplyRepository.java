package me.golf.blog.domain.reply.domain.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyCustomRepository {
    Optional<Reply> findByIdAndMemberId(@Param("replyId") Long replyId, @Param("memberId") Long memberId);
}