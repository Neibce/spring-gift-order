package gift.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class OAuthInfo {

    @Id
    private Long id;

    private String refreshToken;
    private LocalDateTime refreshTokenExpiresAt;

    public OAuthInfo() {
    }

    public OAuthInfo(Long id, String refreshToken, LocalDateTime refreshTokenExpiresAt) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
