package gift.oauth.kakao.controller;

import gift.oauth.kakao.dto.KakaoTokenResponseDto;
import gift.oauth.kakao.service.KakaoOAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

@Controller
@RequestMapping("/oauth/kakao")
public class KakaoOAuthWebController {

    @Value("${oauth.kakao.rest-api-key}")
    private String restApiKey;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    private final KakaoOAuthService kakaoOAuthService;

    public KakaoOAuthWebController(KakaoOAuthService kakaoOAuthService) {
        this.kakaoOAuthService = kakaoOAuthService;
    }

    @GetMapping
    public String showKakaoOAuthPage() {
        return "oauth/kakao/main";
    }

    @GetMapping("/login")
    public String redirectToKakaoOAuth() {
        return "redirect:https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&client_id="
                + restApiKey + "&redirect_uri=" + redirectUri;
    }

    @GetMapping("/callback")
    public String handleKakaoCallback(@RequestParam String code, Model model) {
        try {
            KakaoTokenResponseDto tokenResponseDto = kakaoOAuthService.getToken(code);
            model.addAttribute("accessToken", tokenResponseDto.accessToken());
            model.addAttribute("refreshToken", tokenResponseDto.refreshToken());
            model.addAttribute("expiresIn", tokenResponseDto.expiresIn());
            model.addAttribute("refreshTokenExpiresIn", tokenResponseDto.refreshTokenExpiresIn());
            return "oauth/kakao/callback";
        } catch (HttpClientErrorException e) {
            model.addAttribute("failedStatusCode", e.getStatusCode());
            return "oauth/kakao/error";
        }
    }
}
