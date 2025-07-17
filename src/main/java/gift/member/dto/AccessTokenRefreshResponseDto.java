package gift.member.dto;

public record AccessTokenRefreshResponseDto(
        String accessToken
) {

    public static AccessTokenRefreshResponseDto of(String accessToken) {
        return new AccessTokenRefreshResponseDto(accessToken);
    }
}
