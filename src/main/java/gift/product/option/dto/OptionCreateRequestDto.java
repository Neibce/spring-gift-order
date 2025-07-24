package gift.product.option.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public record OptionCreateRequestDto(
        @NotBlank(message = "옵션 이름은 필수입니다.")
        @Pattern(regexp = "[0-9a-zA-Zㄱ-ㅎ가-힣 ()\\[\\]+\\-&/_]+",
                message = "옵션 이름은 한글, 영문, 숫자, 공백, 특수문자((), [], +, -, &, /, _)만 사용할 수 있습니다.")
        @Length(max = 50, message = "옵션 이름은 최대 50자까지만 입력 가능합니다.")
        String name,

        @NotNull(message = "옵션 수량은 필수입니다.")
        @Range(min = 1, max = 100_000_000, message = "옵션 수량은 1 이상 100,000,000 이하여야 합니다.")
        Integer quantity
) {

    public static OptionCreateRequestDto from(String name, Integer quantity) {
        return new OptionCreateRequestDto(name, quantity);
    }
}
