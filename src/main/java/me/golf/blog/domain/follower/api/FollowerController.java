package me.golf.blog.domain.follower.api;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.follower.application.FollowerService;
import me.golf.blog.domain.follower.dto.FollowerAllResponse;
import me.golf.blog.domain.follower.dto.FollowerCreateResponse;
import me.golf.blog.global.common.PageCustomResponse;
import me.golf.blog.global.common.SliceCustomResponse;
import me.golf.blog.global.security.principal.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(followerService.from(customUserDetails.getId(), toMemberId));
    }

    @GetMapping("/memberId")
    public ResponseEntity<SliceCustomResponse<FollowerAllResponse>> getFollowers(
            @PageableDefault(size = 7, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(followerService.getFollowers(customUserDetails.getId(), pageable));
    }

    @DeleteMapping("/{followId}")
    public ResponseEntity<Void> cancel(
            @PathVariable Long followId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        followerService.cancel(customUserDetails.getId(), followId);

        return ResponseEntity.noContent().build();
    }
}
