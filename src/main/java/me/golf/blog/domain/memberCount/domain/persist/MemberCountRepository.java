package me.golf.blog.domain.memberCount.domain.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberCountRepository extends JpaRepository<MemberCount, Long> {
    @Modifying
    @Query("update MemberCount m set m.followingCount = m.followingCount + 1 where m.id = :id")
    int updateFollowingCount(@Param("id")Long id);

    @Modifying
    @Query("update MemberCount m set m.followerCount = m.followerCount + 1 where m.id = :id")
    int updateFollowerCount(@Param("id") Long id);

    @Modifying
    @Query("update MemberCount m set m.boardCount = m.boardCount + 1 where m.id = :id")
    int updateBoardCount(@Param("id") Long id);
}