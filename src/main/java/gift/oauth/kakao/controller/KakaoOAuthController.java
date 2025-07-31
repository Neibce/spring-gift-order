package gift.oauth.kakao.controller;

import gift.member.dto.MemberLoginResponseDto;
import gift.member.service.MemberService;
import gift.oauth.kakao.dto.KakaoTokenDto;
import gift.oauth.kakao.dto.KakaoUserInfoDto;
import gift.oauth.kakao.service.KakaoOAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final MemberService memberService;

    public KakaoOAuthController(KakaoOAuthService kakaoOAuthService, MemberService memberService) {
        this.kakaoOAuthService = kakaoOAuthService;
        this.memberService = memberService;
    }

    @GetMapping("/callback")
    public ResponseEntity<MemberLoginResponseDto> handleKakaoCallback(@RequestParam String code) {
        var memberLoginResponseDto = kakaoOAuthService.authenticateAndLogin(code);
        return ResponseEntity.ok(memberLoginResponseDto);
    }
}
