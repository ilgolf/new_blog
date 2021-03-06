package me.golf.blog.domain.member.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.golf.blog.domain.member.application.MemberReadService;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.vo.*;
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

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

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
    @GetMapping("/public/members/{email}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable String email) {
        return ResponseEntity.ok().body(memberReadService.findByEmail(Email.from(email)));
    }

    // findAll
    @GetMapping("/public/members")
    public ResponseEntity<PageCustomResponse<MemberAllResponse>> findAll(
            @ModelAttribute MemberSearch memberSearch,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable) {
        return ResponseEntity.ok().body(memberReadService.findAll(memberSearch, pageable));
    }

    // update
    @PatchMapping("/members")
    public ResponseEntity<Void> update(@Valid @RequestBody MemberUpdateRequest request) {
        memberService.update(request.toEntity(), getPrincipal().getId());
        return ResponseEntity.ok().build();
    }

    // delete
    @DeleteMapping("/members")
    public ResponseEntity<Void> delete() {
        memberService.delete(getPrincipal().getId());
        return ResponseEntity.noContent().build();
    }

    private CustomUserDetails getPrincipal() {
        log.debug("principal : {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
