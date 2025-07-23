package gift.product.enums;

import gift.common.SortField;

public enum ProductSortField implements SortField {
    ID("id"),
    NAME("name"),
    PRICE("price"),
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt");

    private final String fieldName;

    ProductSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
