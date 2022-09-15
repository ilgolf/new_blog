package me.golf.blog.global.config;

import lombok.extern.slf4j.Slf4j;
import me.golf.blog.global.security.principal.CustomUserDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Configuration
public class SpringSecurityAuditorAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(authentication -> {
                    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                    boolean isUser = authorities.contains(new SimpleGrantedAuthority("ROLE_USER"));
                    if (isUser) {
                        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
                        return principal.getId();
                    }
                    return null;
                });
    }
}
