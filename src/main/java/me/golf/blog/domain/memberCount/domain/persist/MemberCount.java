package me.golf.blog.domain.memberCount.domain.persist;

import lombok.*;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.global.common.BaseTimeEntity;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@Table(name = "member_count")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class MemberCount extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_count_id", nullable = false)
    private Long id;
    private int followerCount = 0;
    private int followingCount = 0;
    private int boardCount = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberCount memberCount = (MemberCount) o;
        return Objects.equals(this.id, memberCount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}