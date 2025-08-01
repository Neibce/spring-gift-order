package gift.member.entity;

import gift.common.entity.BaseEntity;
import gift.member.enums.AuthProvider;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.util.UUID;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID uuid;

    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private OAuthInfo oAuthInfo;

    private static final Argon2PasswordEncoder PasswordEncoder =
            Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    public Member() {
    }

    private Member(Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
        this.name = builder.name;
        this.authProvider = builder.authProvider;
        this.oAuthInfo = builder.oAuthInfo;
    }

    public boolean verifyPassword(String password) {
        if (authProvider.equals(AuthProvider.KAKAO)) {
            return false;
        }
        return PasswordEncoder.matches(password, this.password);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public OAuthInfo getOAuthInfo() {
        return oAuthInfo;
    }

    public static class Builder {

        private String email;
        private String password;
        private String name;
        private AuthProvider authProvider;
        private OAuthInfo oAuthInfo;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = PasswordEncoder.encode(password);
            return this;
        }

        public Builder authProvider(AuthProvider authProvider) {
            this.authProvider = authProvider;
            return this;
        }

        public Builder oAuthToken(OAuthInfo oAuthInfo) {
            this.oAuthInfo = oAuthInfo;
            return this;
        }

        public Member build() {
            return new Member(this);
        }
    }
}
