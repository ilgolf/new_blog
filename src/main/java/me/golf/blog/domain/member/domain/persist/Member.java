package me.golf.blog.domain.member.domain.persist;

import lombok.*;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.boardCount.domain.persist.BoardCount;
import me.golf.blog.domain.follower.domain.persist.Follower;
import me.golf.blog.domain.member.domain.vo.*;
import me.golf.blog.domain.memberCount.domain.persist.MemberCount;
import me.golf.blog.global.common.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@DynamicUpdate
@DynamicInsert
@Where(clause = "activated = true")
@Table(indexes = @Index(name = "i_email", columnList = "email"))
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false, updatable = false)
    private Long id;

    @Embedded
    private Email email; // prefix : ex) ilgolc, suffix : ex) naver.com

    @Embedded
    private Password password;

    @Embedded
    private Name name; // firstName LastName

    @Embedded
    private Nickname nickname;

    @Column(nullable = false)
    private LocalDate birth;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @Column(name = "activated")
    @Builder.Default
    private Boolean activated = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_count_id")
    private MemberCount memberCount;

    // == 연관관계 로직 == //
    public void addMemberCount(final MemberCount memberCount) {
        this.memberCount = memberCount;
    }

    // == 비즈니스 로직 == //
    public Member encode(final PasswordEncoder encoder) {
        password = Password.encode(password.password(), encoder);
        return this;
    }

    public Member update(final Member member, final PasswordEncoder encoder) {
        this.password = Password.encode(member.getPassword().password(), encoder);
        this.nickname = member.getNickname();
        this.name = member.getName();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void delete() {
        activated = false;
        recordDeleteTime();
    }
}