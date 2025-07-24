package gift.wishlist.enums;

import gift.common.SortField;

public enum WishlistItemSortField implements SortField {
    PRODUCT_ID("product.id"),
    PRODUCT_NAME("product.name"),
    PRODUCT_PRICE("product.price"),
    PRODUCT_CREATED_AT("product.createdAt"),
    PRODUCT_UPDATED_AT("product.updatedAt"),

    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt");

    private final String fieldName;

    WishlistItemSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
