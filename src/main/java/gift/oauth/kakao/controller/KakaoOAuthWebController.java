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

    @Value("${KAKAO_REST_API_KEY}")
    private String restApiKey;

    private final String redirectUri = "http://localhost:8080/oauth/kakao/callback";
    private final KakaoOAuthService kakaoOAuthService;

    public KakaoOAuthWebController(KakaoOAuthService kakaoOAuthService) {
        this.kakaoOAuthService = kakaoOAuthService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("restApiKey", restApiKey);
        model.addAttribute("redirectUri", redirectUri);
        return "oauth/kakao/login";
    }

    @GetMapping("/callback")
    public String callbackPage(@RequestParam String code, Model model) {
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
