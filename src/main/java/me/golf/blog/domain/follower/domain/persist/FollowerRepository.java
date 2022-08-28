package me.golf.blog.domain.follower.domain.persist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Long>, FollowerCustomRepository {

    Optional<Follower> findByIdAndFromMember(final Long id, final Long fromMember);
}