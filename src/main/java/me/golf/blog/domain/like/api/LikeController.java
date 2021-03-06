package me.golf.blog.domain.like.api;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.like.application.LikeService;
import me.golf.blog.global.security.principal.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{boardId}")
    public ResponseEntity<Long> likeBoard(@PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(likeService.likeBoard(boardId, getPrincipal().getId()));
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<Void> unLikeBoard(@PathVariable Long likeId) {
        likeService.unLikeBoard(likeId);
        return ResponseEntity.noContent().build();
    }

    private CustomUserDetails getPrincipal() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
