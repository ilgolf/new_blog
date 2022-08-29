package me.golf.blog.domain.member.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.golf.blog.domain.member.application.MemberReadService;
import me.golf.blog.domain.member.dto.*;
import me.golf.blog.global.common.PageCustomResponse;
import me.golf.blog.global.security.principal.CustomUserDetails;
import me.golf.blog.domain.member.application.MemberService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final MemberReadService memberReadService;

    // create
    @PostMapping("/public/members")
    public ResponseEntity<JoinResponse> create(@Valid @RequestBody JoinRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.create(request.toEntity()));
    }

    // read
    @GetMapping("/members/detail")
    public ResponseEntity<MemberResponse> getDetailById() {
        return ResponseEntity.ok().body(memberReadService.getDetailBy(this.getMemberId()));
    }

    // findAll
    @GetMapping("/public/members")
    public ResponseEntity<PageCustomResponse<MemberAllResponse>> getMembers(
            @ModelAttribute MemberSearch memberSearch,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable) {
        return ResponseEntity.ok().body(memberReadService.getMembers(memberSearch, pageable));
    }

    // update
    @PatchMapping("/members")
    public ResponseEntity<Void> update(@Valid @RequestBody MemberUpdateRequest request) {
        memberService.update(request.toEntity(), this.getMemberId());
        return ResponseEntity.ok().build();
    }

    // delete
    @DeleteMapping("/members")
    public ResponseEntity<Void> delete() {
        memberService.delete(this.getMemberId());
        return ResponseEntity.noContent().build();
    }

    private Long getMemberId() {
        log.debug("principal : {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return principal.getId();
    }
}
