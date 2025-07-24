package gift.oauth.kakao.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oauth/kakao")
public class KakaoOAuthWebController {

    @Value("${KAKAO_REST_API_KEY}")
    private String restApiKey;

    private final String redirectUri = "http://localhost:8080/oauth/kakao/callback";

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("restApiKey", restApiKey);
        model.addAttribute("redirectUri", redirectUri);
        return "oauth/kakao/login";
    }
}
