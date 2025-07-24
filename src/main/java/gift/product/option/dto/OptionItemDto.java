package gift.product.option.dto;

import gift.product.option.entity.Option;

public record OptionItemDto(
        Long id,
        String name,
        Integer quantity
) {
    public static OptionItemDto of(Option option) {
        return new OptionItemDto(option.getId(), option.getName(), option.getQuantity());

    }
}
