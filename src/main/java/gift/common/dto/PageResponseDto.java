package gift.common.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponseDto<T>(
        List<T> content,
        int page,
        int size,
        long totalElements
) {
    public static <T> PageResponseDto<T> from(Page<T> page) {
        return new PageResponseDto<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements()
        );
    }
}
