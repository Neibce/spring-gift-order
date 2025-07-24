package gift.product.entity;

import gift.common.entity.BaseEntity;
import gift.product.dto.ProductCreateRequestDto;
import gift.product.dto.ProductUpdateRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(max = 15)
    @Pattern(regexp = "[0-9a-zA-Zㄱ-ㅎ가-힣 ()\\[\\]+\\-&/_]+")
    @Column(nullable = false)
    private String name;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Long price;

    @URL
    private String imageUrl;

    public Product() {
    }

    public Product(ProductCreateRequestDto dto) {
        this.name = dto.name();
        this.price = dto.price();
        this.imageUrl = dto.imageUrl();
    }

    public void update(ProductUpdateRequestDto dto) {
        if (dto.name() != null) {
            this.name = dto.name();
        }
        if (dto.price() != null) {
            this.price = dto.price();
        }
        if (dto.imageUrl() != null) {
            this.imageUrl = dto.imageUrl();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
