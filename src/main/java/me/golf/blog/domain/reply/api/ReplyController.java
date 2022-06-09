package me.golf.blog.domain.reply.api;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.reply.applicationa.ReplyService;
import me.golf.blog.domain.reply.dto.ReplyAllResponse;
import me.golf.blog.domain.reply.dto.ReplyCreateRequest;
import me.golf.blog.global.security.principal.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping("/replies/{boardId}")
    public ResponseEntity<Long> create(@Valid @RequestBody ReplyCreateRequest request, @PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.create(request, boardId, getPrincipal().getId()));
    }

    @GetMapping("/public/replies/{boardId}")
    public ResponseEntity<List<ReplyAllResponse>> findAll(@PageableDefault Pageable pageable, @PathVariable Long boardId) {
        return ResponseEntity.ok().body(replyService.findAll(pageable, boardId));
    }

    @PatchMapping("/replies/{replyId}")
    public ResponseEntity<Void> update(@Valid @RequestBody ReplyUpdateRequest request, @PathVariable final Long replyId) {
        replyService.update(request.getComment(), replyId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<Void> delete(@PathVariable final Long replyId) {
        replyService.deleteById(replyId);
        return ResponseEntity.noContent().build();
    }

    private CustomUserDetails getPrincipal() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
