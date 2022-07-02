package me.golf.blog.domain.board.api;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.application.BoardReadService;
import me.golf.blog.domain.board.application.BoardService;
import me.golf.blog.domain.board.domain.persist.SearchKeywordRequest;
import me.golf.blog.domain.board.dto.*;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.global.common.PageCustomResponse;
import me.golf.blog.global.security.principal.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class BoardController {
    private final BoardService boardService;
    private final BoardReadService boardReadService;

    @PostMapping("/boards")
    public ResponseEntity<Long> create(@Valid @RequestBody BoardCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.create(request.toEntity(), getPrincipal().getId()));
    }

    @GetMapping("/public/boards/id/{boardId}")
    public ResponseEntity<BoardResponse> findById(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardReadService.findById(boardId));
    }

    @GetMapping("/public/boards")
    public ResponseEntity<PageCustomResponse<BoardAllResponse>> findAll(
            @ModelAttribute SearchKeywordRequest keyword,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(boardReadService.findAll(keyword, pageable));
    }

    @GetMapping("/public/boards/email/{email}")
    public ResponseEntity<PageCustomResponse<BoardAllResponse>> findByEmail(
            @PathVariable String email,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(boardReadService.findByEmail(Email.from(email), pageable));
    }

    @PatchMapping("/boards/{boardId}")
    public ResponseEntity<Void> update(@Valid @RequestBody BoardUpdateRequest request,
                                       @PathVariable Long boardId) {
        boardService.update(request.toEntity(), boardId, getPrincipal().getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/boards/{boardsId}")
    public ResponseEntity<Void> delete(@PathVariable Long boardsId) {
        boardService.delete(boardsId, getPrincipal().getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/boards/temp-board")
    public ResponseEntity<Long> createTemp(@Valid @RequestBody TempBoardCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.createTemp(request.toEntity(), getPrincipal().getId()));
    }

    @GetMapping("/boards/temp-board")
    public ResponseEntity<PageCustomResponse<TempBoardListResponse>> getTempBoardList(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(boardReadService.getTempBoardList(getPrincipal().getId(), pageable));
    }

    @GetMapping("/boards/temp-board/{boardId}")
    public ResponseEntity<TempDetailResponse> getTempBoardDetail(@PathVariable Long boardId) {
        return ResponseEntity.ok().body(boardReadService.getTempBoard(boardId, getPrincipal().getId()));
    }

    @DeleteMapping("/boards/temp-board/{boardId}")
    public ResponseEntity<Void> deleteTempBoard(@PathVariable Long boardId) {
        boardReadService.deleteTempBoard(boardId, getPrincipal().getId());
        return ResponseEntity.noContent().build();
    }

    private CustomUserDetails getPrincipal() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}