package me.golf.blog.global.security.principal;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.global.config.RedisPolicy;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    public static final MemberNotFoundException NOT_FOUND_EXCEPTION =
            new MemberNotFoundException(ErrorCode.USER_NOT_FOUND);
    private final MemberRepository memberRepository;

    @Override
    @Cacheable(value = RedisPolicy.AUTH_KEY, key = "#id")
    public CustomUserDetails loadUserByUsername(final String id) throws UsernameNotFoundException {
        return memberRepository.findByIdWithDetails(Long.valueOf(id))
                .orElseThrow(() -> NOT_FOUND_EXCEPTION);
    }
}
