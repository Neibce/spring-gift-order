package gift.oauth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoAccountDto(
        @JsonProperty("id")
        Long id,

        @JsonProperty("email")
        String email,

        @JsonProperty("profile")
        Profile profile

) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Profile(
            @JsonProperty("nickname")
            String nickname

    ) {

    }
}
