package gift.member.service;

import gift.common.util.TokenUtils;
import gift.exception.EntityAlreadyExistsException;
import gift.exception.EntityNotFoundException;
import gift.exception.InvalidCredentialsException;
import gift.member.dto.AccessTokenRefreshResponseDto;
import gift.member.dto.MemberDto;
import gift.member.dto.MemberLoginRequestDto;
import gift.member.dto.MemberLoginResponseDto;
import gift.member.dto.MemberRegisterRequestDto;
import gift.member.dto.MemberTokenDto;
import gift.member.dto.AccessTokenRefreshRequestDto;
import gift.member.entity.Member;
import gift.member.entity.OAuthInfo;
import gift.member.enums.AuthProvider;
import gift.member.repository.MemberRepository;
import gift.oauth.kakao.dto.KakaoTokenDto;
import gift.oauth.kakao.dto.KakaoUserInfoDto;
import gift.token.service.TokenProvider;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public MemberService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public MemberDto register(MemberRegisterRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.email())) {
            throw new EntityAlreadyExistsException("이미 가입된 계정입니다.");
        }

        Member member = new Member.Builder()
                .email(requestDto.email())
                .name(requestDto.name())
                .password(requestDto.password())
                .authProvider(AuthProvider.LOCAL)
                .build();

        memberRepository.save(member);
        return MemberDto.from(member);
    }

    @Transactional
    public MemberDto register(KakaoUserInfoDto kakaoUserInfoDto, KakaoTokenDto kakaoTokenDto) {
        if (memberRepository.existsByEmail(kakaoUserInfoDto.kakaoAccount().email())) {
            throw new EntityAlreadyExistsException("이미 가입된 계정입니다.");
        }

        Member member = new Member.Builder()
                .email(kakaoUserInfoDto.kakaoAccount().email())
                .name(kakaoUserInfoDto.kakaoAccount().profile().nickname())
                .password(" ")
                .authProvider(AuthProvider.KAKAO)
                .oAuthToken(new OAuthInfo(
                        kakaoUserInfoDto.id(),
                        kakaoTokenDto.refreshToken(),
                        TokenUtils.calculateExpiryDateTime(kakaoTokenDto.expiresIn())))
                .build();

        memberRepository.save(member);
        return MemberDto.from(member);
    }

    @Transactional
    public MemberLoginResponseDto login(MemberLoginRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!member.verifyPassword(requestDto.password())) {
            throw new InvalidCredentialsException();
        }

        MemberTokenDto memberTokenDto = MemberTokenDto.of(
                tokenProvider.generateAccessToken(member),
                tokenProvider.generateRefreshToken(member));
        return MemberLoginResponseDto.of(MemberDto.from(member), memberTokenDto);
    }

    @Transactional
    public MemberLoginResponseDto login(KakaoUserInfoDto kakaoUserInfoDto) {
        Member member = memberRepository.findByoAuthInfoIdAndAuthProvider(kakaoUserInfoDto.id(), AuthProvider.KAKAO)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        MemberTokenDto memberTokenDto = MemberTokenDto.of(
                tokenProvider.generateAccessToken(member),
                tokenProvider.generateRefreshToken(member));
        return MemberLoginResponseDto.of(MemberDto.from(member), memberTokenDto);
    }

    public AccessTokenRefreshResponseDto refreshAccessToken(
            AccessTokenRefreshRequestDto requestDto) {
        UUID memberUuid = tokenProvider.getMemberUuidFromRefreshToken(requestDto.refreshToken());
        String newAccessToken = tokenProvider.generateAccessToken(findMemberByUuid(memberUuid));

        return AccessTokenRefreshResponseDto.of(newAccessToken);
    }

    public Member findMemberByUuid(UUID uuid) throws EntityNotFoundException {
        return memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
    }

    public boolean isMemberExistsByOAuthInfoId(Long id) {
        return memberRepository.existsByoAuthInfoId(id);
    }
}
