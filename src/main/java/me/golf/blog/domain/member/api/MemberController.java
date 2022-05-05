package me.golf.blog.domain.member.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.golf.blog.domain.member.dto.MemberUpdateRequest;
import me.golf.blog.global.security.principal.CustomUserDetails;
import me.golf.blog.domain.member.application.MemberService;
import me.golf.blog.domain.member.dto.JoinRequest;
import me.golf.blog.domain.member.dto.JoinResponse;
import me.golf.blog.domain.member.dto.MemberResponse;
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

    // create
    @PostMapping("/public/members")
    public ResponseEntity<JoinResponse> create(@Valid @RequestBody JoinRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.create(request.toEntity()));
    }

    // read
    @GetMapping("/members/id")
    public ResponseEntity<MemberResponse> findMember() {
        return ResponseEntity.ok().body(memberService.findOne(getPrincipal().getId()));
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
