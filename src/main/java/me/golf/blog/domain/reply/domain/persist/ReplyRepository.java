package me.golf.blog.domain.reply.domain.persist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyCustomRepository {
}