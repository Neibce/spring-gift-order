package gift.member.dto;

import gift.member.entity.Member;
import java.time.LocalDateTime;
import java.util.UUID;

public record MemberDto(
        UUID uuid,
        String email,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static MemberDto from(Member member) {
        return new MemberDto(member.getUuid(), member.getEmail(), member.getName(),
                member.getCreatedAt(), member.getUpdatedAt());
    }
}
