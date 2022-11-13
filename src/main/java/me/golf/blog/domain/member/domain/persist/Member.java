package me.golf.blog.domain.member.domain.persist;

import lombok.*;
import me.golf.blog.domain.member.domain.vo.*;
import me.golf.blog.global.common.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Entity
@DynamicUpdate
@DynamicInsert
@Where(clause = "activated = true")
@Table(indexes = {
        @Index(name = "i_email", columnList = "email"),
        @Index(name = "i_nickname", columnList = "nickname")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false, updatable = false)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    private Name name;

    @Embedded
    private Nickname nickname;

    @Column(nullable = false)
    private LocalDate birth;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @Column(name = "activated")
    private Boolean activated = true;

    @Embedded
    private MemberCount memberCount;

    @Builder
    private Member(Long id, Email email, Password password, Name name, Nickname nickname,
                   LocalDate birth, RoleType role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.role = role;
        this.activated = true;
        this.memberCount = new MemberCount();
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

    public void delete() {
        activated = false;
        recordDeleteTime();
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
}