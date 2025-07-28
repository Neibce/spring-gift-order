package gift.common.dto;

import gift.common.SortField;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public record PageRequestDto(
        @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
        Integer page,

        @Range(min = 1, max = 100, message = "페이지 크기는 1 이상 100 이하여야 합니다.")
        Integer size,

        String sortBy,

        Boolean ascending
) {

    public <T extends Enum<T> & SortField> PageRequest toPageable(
            Class<T> enumClass, T defaultSortField) {
        int page = this.page != null ? this.page : 1;
        int size = this.size != null ? this.size : 10;
        T sortField = defaultSortField;
        boolean ascending = this.ascending != null ? this.ascending : true;

        if (sortBy != null) {
            for (T enumValue : enumClass.getEnumConstants()) {
                if (enumValue.getFieldName().equals(sortBy)) {
                    sortField = enumValue;
                }
            }
        }

        return PageRequest.of(page - 1, size,
                Sort.by(
                        ascending ? Sort.Direction.ASC : Sort.Direction.DESC,
                        sortField.getFieldName()
                )
        );
    }
}
