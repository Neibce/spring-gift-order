package gift;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import gift.oauth.kakao.dto.KakaoTokenResponseDto;
import gift.oauth.kakao.service.KakaoOAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class KakaoOAuthWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KakaoOAuthService kakaoOAuthService;

    @Test
    void 카카오_OAuth_메인페이지_표시() throws Exception {
        mockMvc.perform(get("/oauth/kakao"))
                .andExpect(status().isOk())
                .andExpect(view().name("oauth/kakao/main"));
    }

    @Test
    void 카카오_OAuth_로그인_리다이렉트() throws Exception {
        mockMvc.perform(get("/oauth/kakao/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(
                    "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&client_id=test-api-key&redirect_uri=http://localhost:8080/oauth/kakao/callback"
                ));
    }

    @Test
    void 카카오_OAuth_콜백_성공() throws Exception {
        var tokenResponse = new KakaoTokenResponseDto(
            "test_access_token",
            "test_refresh_token",
            "bearer",
            3600,
            86400,
            "talk_message"
        );
        given(kakaoOAuthService.getToken("test_auth_code")).willReturn(tokenResponse);

        mockMvc.perform(get("/oauth/kakao/callback").param("code", "test_auth_code"))
                .andExpect(status().isOk())
                .andExpect(view().name("oauth/kakao/callback"))
                .andExpect(model().attribute("accessToken", "test_access_token"))
                .andExpect(model().attribute("refreshToken", "test_refresh_token"))
                .andExpect(model().attribute("expiresIn", 3600))
                .andExpect(model().attribute("refreshTokenExpiresIn", 86400));
    }

    @Test
    void 카카오_OAuth_콜백_실패_HttpClientErrorException() throws Exception {
        var exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Kakao OAuth token 요청 실패");
        when(kakaoOAuthService.getToken("invalid_code")).thenThrow(exception);

        mockMvc.perform(get("/oauth/kakao/callback").param("code", "invalid_code"))
                .andExpect(status().isOk())
                .andExpect(view().name("oauth/kakao/error"))
                .andExpect(model().attribute("failedStatusCode", HttpStatus.BAD_REQUEST));
    }
} 