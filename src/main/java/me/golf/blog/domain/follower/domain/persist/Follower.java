package me.golf.blog.domain.follower.domain.persist;

import lombok.*;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.global.common.BaseTimeEntity;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(indexes = {
        @Index(name = "i_to_member", columnList = "to_member"),
        @Index(name = "i_from_member", columnList = "from_member")
})
public class Follower extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follower_id", nullable = false)
    private Long id;

    @Column(name = "to_member", nullable = false)
    private Long toMember;

    @Column(name = "from_member", nullable = false)
    private Long fromMember;
}