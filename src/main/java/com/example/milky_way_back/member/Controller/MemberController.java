package com.example.milky_way_back.member.Controller;


import com.example.milky_way_back.member.Dto.*;
import com.example.milky_way_back.member.Service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@Tag(name = "회원 관련 API", description = "등록 및 확인, 토큰 관련 API")
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<StatusResponse> signup(@RequestBody SignupRequest request) {
        return memberService.signup(request);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(memberService.login(loginRequest));
    }

    // 중복 확인
    @PostMapping("/signup/checkId")
    public ResponseEntity<StatusResponse> checkId(@RequestBody IdRequest idRequest) {
        return memberService.duplicationIdCheck(idRequest);
    }

    // 리프레시 토큰
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(HttpServletRequest request) {
        return ResponseEntity.ok(memberService.reissue(request));
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<StatusResponse> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }

}