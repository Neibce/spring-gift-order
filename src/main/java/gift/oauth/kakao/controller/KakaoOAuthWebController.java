package gift.oauth.kakao.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oauth/kakao")
public class KakaoOAuthWebController {
    @Value("${oauth.kakao.rest-api-key}")
    private String restApiKey;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    @GetMapping
    public String showKakaoOAuthPage() {
        return "oauth/kakao/main";
    }

    @GetMapping("/login")
    public String redirectToKakaoOAuth() {
        return "redirect:https://kauth.kakao.com/oauth/authorize?scope=account_email,profile_nickname,talk_message&response_type=code&client_id="
                + restApiKey + "&redirect_uri=" + redirectUri;
    }
}
