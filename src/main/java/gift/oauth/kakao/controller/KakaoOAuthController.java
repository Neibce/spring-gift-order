package gift.oauth.kakao.controller;

import gift.member.dto.MemberLoginResponseDto;
import gift.oauth.kakao.service.KakaoOAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth/kakao")
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;

    public KakaoOAuthController(KakaoOAuthService kakaoOAuthService) {
        this.kakaoOAuthService = kakaoOAuthService;
    }

    @GetMapping("/callback")
    public ResponseEntity<MemberLoginResponseDto> handleKakaoCallback(@RequestParam String code) {
        var memberLoginResponseDto = kakaoOAuthService.authenticateAndLogin(code);
        return ResponseEntity.ok(memberLoginResponseDto);
    }
}
