package gift.order.dto;

import jakarta.validation.constraints.NotNull;

public record OrderRequestDto(
        @NotNull
        Long optionId,
        Integer quantity,
        String message
) {

}
