package gift.order.dto;

import gift.order.entity.Order;
import java.time.LocalDateTime;

public record OrderInfoDto(
        Long id,
        Long optionId,
        Integer quantity,
        LocalDateTime orderDateTime,
        String message
) {

    public static OrderInfoDto from(Order order) {
        return new OrderInfoDto(
                order.getId(),
                order.getOption().getId(),
                order.getQuantity(),
                order.getCreatedAt(),
                order.getMessage()
        );
    }
}
