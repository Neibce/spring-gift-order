package gift.member.dto;

public record AccessTokenRefreshRequestDto(
        String refreshToken
) {

    public static AccessTokenRefreshRequestDto of(String accessToken) {
        return new AccessTokenRefreshRequestDto(accessToken);
    }
}
