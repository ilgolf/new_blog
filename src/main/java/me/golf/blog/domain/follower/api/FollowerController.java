package me.golf.blog.domain.follower.api;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.follower.application.FollowerService;
import me.golf.blog.domain.follower.dto.FollowerCreateResponse;
import me.golf.blog.global.security.principal.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/followers")
public class FollowerController {

    private final FollowerService followerService;

    // from
    @PostMapping("/{toMemberId}")
    public ResponseEntity<FollowerCreateResponse> from(
            @PathVariable Long toMemberId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long fromMemberId = customUserDetails.getId();

        return ResponseEntity.status(HttpStatus.CREATED).body(followerService.from(fromMemberId, toMemberId));
    }
}
