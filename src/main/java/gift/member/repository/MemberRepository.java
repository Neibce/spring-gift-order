package gift.member.repository;

import gift.member.entity.Member;
import gift.member.enums.AuthProvider;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUuid(UUID uuid);

    Optional<Member> findByoAuthInfoIdAndAuthProvider(Long id, AuthProvider authProvider);

    boolean existsByoAuthInfoId(Long id);
}
