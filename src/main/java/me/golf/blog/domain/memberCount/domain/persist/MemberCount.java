package me.golf.blog.domain.memberCount.domain.persist;

import lombok.*;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.global.common.BaseTimeEntity;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@Table(name = "member_count")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCount extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_count_id", nullable = false)
    private Long id;

    @Builder.Default
    private int followerCount = 0;

    @Builder.Default
    private int followingCount = 0;

    @Builder.Default
    private int boardCount = 0;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Member member;

    public static MemberCount createMemberCount(final Member member) {
        MemberCount memberCount = new MemberCount();
        memberCount.addMember(member);
        return memberCount;
    }

    public void addMember(final Member member) {
        this.member = member;
        member.addMemberCount(this);
    }

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