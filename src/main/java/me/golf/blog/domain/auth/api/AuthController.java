package me.golf.blog.domain.auth.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.golf.blog.domain.auth.application.AuthService;
import me.golf.blog.domain.auth.dto.SimpleAuthResponse;
import me.golf.blog.domain.auth.dto.LoginRequest;
import me.golf.blog.global.jwt.dto.TokenDTO;
import me.golf.blog.global.jwt.vo.RefreshToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/public/auth/login")
    public ResponseEntity<SimpleAuthResponse> login(@RequestBody LoginRequest request) {
        TokenDTO token = authService.login(request.getEmail(), request.getPassword());
        RefreshToken refreshToken = token.getRefreshToken();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, getCookie(refreshToken).toString())
                .body(SimpleAuthResponse.from(token.getAccessToken(), true));
    }

    @PostMapping("/public/auth/reissue")
    public ResponseEntity<SimpleAuthResponse> reissue(@CookieValue(name = "refreshToken") String refreshToken) {
        log.debug("token : {}", refreshToken);
        return ResponseEntity.ok(SimpleAuthResponse.from(authService.reissue(refreshToken), true));
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie myCookie = new Cookie("refreshToken", null);
        myCookie.setMaxAge(0);
        myCookie.setPath("/");
        response.addCookie(myCookie);

        return ResponseEntity.noContent().build();
    }

    private ResponseCookie getCookie(RefreshToken refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(18000)
                .build();
    }
}
