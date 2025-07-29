package gift.oauth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoUserInfoDto(
        @JsonProperty("id")
        Long id,

        @JsonProperty("connected_at")
        LocalDateTime connectedAt,

        @JsonProperty("kakao_account")
        KakaoAccountDto kakaoAccount
) {

}
