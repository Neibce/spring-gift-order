package gift.wishlist.repository;

import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.wishlist.entity.WishlistItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {

    @EntityGraph(attributePaths = {"product"})
    List<WishlistItem> getWishlistItemsByMemberUuid(UUID memberUuid);

    boolean existsByMemberUuidAndProductId(UUID memberUuid, Long productId);

    void deleteByMemberUuidAndProductId(UUID memberUuid, Long productId);

    Optional<WishlistItem> getWishlistItemByMemberAndProduct(Member member, Product product);
}
