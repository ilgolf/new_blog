package me.golf.blog.domain.auth.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.member.domain.persist.MemberCustomRepositoryImpl;
import me.golf.blog.global.jwt.error.TokenNotFoundException;
import me.golf.blog.global.jwt.vo.AccessToken;
import me.golf.blog.global.security.principal.CustomUserDetails;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Password;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;
import me.golf.blog.global.jwt.TokenProvider;
import me.golf.blog.global.jwt.dto.TokenDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final MemberCustomRepositoryImpl memberQueryRepository;
    private final AuthenticationManagerBuilder managerBuilder;
    private final TokenProvider tokenProvider;

    public TokenDTO login(final Email email, final Password password) {
        final String userPw = password.password();

        CustomUserDetails userDetails = memberQueryRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(userDetails, userPw);

        Authentication authenticate = managerBuilder.getObject().authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        return tokenProvider.createToken(userDetails.getId(), authenticate);
    }

    public AccessToken reissue(final String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new TokenNotFoundException(ErrorCode.TOKEN_NOT_FOUND);
        }

        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.createToken(principal.getId(), authentication).getAccessToken();
    }
}
