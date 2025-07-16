package gift.member.entity;

import gift.member.dto.MemberRegisterRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@Entity
public class Member {

    @Id
    @GeneratedValue
    private UUID uuid;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private static final Argon2PasswordEncoder PasswordEncoder =
            Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    public Member() {
    }

    public Member(MemberRegisterRequestDto requestDto) {
        this.email = requestDto.email();
        this.password = PasswordEncoder.encode(requestDto.password());
        this.name = requestDto.name();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public boolean verifyPassword(String password) {
        return PasswordEncoder.matches(password, this.password);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
