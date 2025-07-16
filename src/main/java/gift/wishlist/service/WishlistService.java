package gift.wishlist.service;

import gift.exception.EntityNotFoundException;
import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.product.service.ProductService;
import gift.wishlist.dto.WishlistItemResponseDto;
import gift.wishlist.dto.WishlistUpdateRequestDto;
import gift.wishlist.entity.WishlistItem;
import gift.wishlist.repository.WishlistRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;

    public WishlistService(WishlistRepository wishlistRepository,
            ProductService productService) {
        this.wishlistRepository = wishlistRepository;
        this.productService = productService;
    }

    @Transactional
    public WishlistItemResponseDto upsertWishlistItem(Member member, Long productId,
            WishlistUpdateRequestDto requestDto) {
        Product product = productService.getProductById(productId);

        WishlistItem wishlistItem = wishlistRepository
                .getWishlistItemByMemberAndProduct(member, product)
                .orElse(new WishlistItem(member, product, requestDto.quantity()));
        wishlistItem.setQuantity(requestDto.quantity());

        wishlistRepository.save(wishlistItem);
        return new WishlistItemResponseDto(wishlistItem);
    }

    public List<WishlistItemResponseDto> getWishlistItems(Member member) {
        return wishlistRepository.getWishlistItemsByMemberUuid(member.getUuid()).stream()
                .map(WishlistItemResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void deleteWishlistItem(Member member, Long productId) {
        if (!wishlistRepository.existsByMemberUuidAndProductId(member.getUuid(), productId)) {
            throw new EntityNotFoundException("위시리스트 항목을 찾을 수 없습니다.");
        }

        wishlistRepository.deleteByMemberUuidAndProductId(member.getUuid(), productId);
    }
}
