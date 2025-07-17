package gift.member.dto;

public record MemberLoginResponseDto(
        MemberDto memberDto,
        MemberTokenDto tokenInfo
) {

    public static MemberLoginResponseDto of(MemberDto memberDto, MemberTokenDto tokenInfo) {
        return new MemberLoginResponseDto(memberDto, tokenInfo);
    }
}
