package gift.wishlist.dto;

import gift.product.dto.ProductItemDto;
import gift.wishlist.entity.WishlistItem;
import java.time.LocalDateTime;

public record WishlistItemResponseDto(
        ProductItemDto product,
        int quantity,
        LocalDateTime createdAt
) {

    public static WishlistItemResponseDto of(ProductItemDto product, int quantity,
            LocalDateTime addedAt) {
        return new WishlistItemResponseDto(product, quantity, addedAt);
    }

    public static WishlistItemResponseDto from(WishlistItem wishlistItem) {
        return new WishlistItemResponseDto(
                ProductItemDto.from(wishlistItem.getProduct()),
                wishlistItem.getQuantity(),
                wishlistItem.getCreatedAt()
        );
    }
}
