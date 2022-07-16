package me.golf.blog.domain.member;

import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.vo.RoleType;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Name;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.domain.vo.Password;
import me.golf.blog.global.security.principal.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.LocalDate;
import java.util.List;

public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {
    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        Member member = Member.builder()
                .id(annotation.id())
                .email(Email.from(annotation.email()))
                .password(Password.from(annotation.password()))
                .name(Name.from(annotation.name()))
                .nickname(Nickname.from(annotation.nickname()))
                .role(RoleType.USER)
                .birth(LocalDate.of(1999, 10, 25))
                .build();

        List<GrantedAuthority> role =
                AuthorityUtils.createAuthorityList(RoleType.USER.name());

        CustomUserDetails userDetails = CustomUserDetails.of(member);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, annotation.password(), role));

        return SecurityContextHolder.getContext();
    }
}
