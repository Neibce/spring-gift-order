package gift.wishlist.repository;

import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.wishlist.entity.WishlistItem;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {

    @EntityGraph(attributePaths = {"product"})
    Page<WishlistItem> getWishlistItemsByMemberUuid(UUID memberUuid, Pageable pageable);

    boolean existsByMemberUuidAndProductId(UUID memberUuid, Long productId);

    void deleteByMemberUuidAndProductId(UUID memberUuid, Long productId);

    Optional<WishlistItem> getWishlistItemByMemberAndProduct(Member member, Product product);
}
