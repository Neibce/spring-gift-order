package gift.oauth.kakao.service;

import gift.oauth.kakao.dto.KakaoTokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoOAuthService {

    @Value("${KAKAO_REST_API_KEY}")
    private String restApiKey;

    private final String redirectUri = "http://localhost:8080/oauth/kakao/callback";
    private final RestTemplate restTemplate;

    public KakaoOAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public KakaoTokenResponseDto getToken(String code)
            throws HttpClientErrorException, ResourceAccessException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", restApiKey);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<KakaoTokenResponseDto> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token", request, KakaoTokenResponseDto.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new HttpClientErrorException(response.getStatusCode(), "Kakao OAuth token 요청 실패");
        }
        return response.getBody();
    }

}
