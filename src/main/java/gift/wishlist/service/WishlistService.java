package gift.wishlist.service;

import gift.common.dto.PageResponseDto;
import gift.exception.EntityNotFoundException;
import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.product.service.ProductService;
import gift.wishlist.dto.WishlistItemResponseDto;
import gift.wishlist.dto.WishlistUpdateRequestDto;
import gift.wishlist.entity.WishlistItem;
import gift.wishlist.repository.WishlistRepository;
import org.springframework.data.domain.Pageable;
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
        return WishlistItemResponseDto.from(wishlistItem);
    }

    public PageResponseDto<WishlistItemResponseDto> getWishlistItems(
            Member member, Pageable pageable) {
        return PageResponseDto.from(
                wishlistRepository.getWishlistItemsByMemberUuid(member.getUuid(), pageable)
                        .map(WishlistItemResponseDto::from)
        );
    }

    @Transactional
    public void deleteWishlistItem(Member member, Long productId) {
        if (!wishlistRepository.existsByMemberUuidAndProductId(member.getUuid(), productId)) {
            throw new EntityNotFoundException("위시리스트 항목을 찾을 수 없습니다.");
        }

        wishlistRepository.deleteByMemberUuidAndProductId(member.getUuid(), productId);
    }

    @Transactional
    public void deleteIfExists(Member member, Long productId) {
        if (!wishlistRepository.existsByMemberUuidAndProductId(member.getUuid(), productId)) {
            return;
        }

        wishlistRepository.deleteByMemberUuidAndProductId(member.getUuid(), productId);
    }
}
