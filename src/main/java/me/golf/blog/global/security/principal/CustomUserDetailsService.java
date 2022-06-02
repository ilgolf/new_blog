package me.golf.blog.global.security.principal;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.member.domain.persist.MemberQueryRepository;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberQueryRepository memberQueryRepository;
    public static final MemberNotFoundException NOT_FOUND_EXCEPTION =
            new MemberNotFoundException(ErrorCode.USER_NOT_FOUND);

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return memberQueryRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> NOT_FOUND_EXCEPTION);
    }
}
