package gift.product.controller;

import gift.common.dto.PageRequestDto;
import gift.common.dto.PageResponseDto;
import gift.product.enums.ProductSortField;
import gift.product.dto.ProductCreateRequestDto;
import gift.product.dto.ProductItemDto;
import gift.product.dto.ProductUpdateRequestDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductItemDto> createProduct(
            @Valid @RequestBody ProductCreateRequestDto product) {
        var createdProductItemDto = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProductItemDto);
    }

    @PatchMapping("{id}")
    public ResponseEntity<ProductItemDto> updateProduct(@PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequestDto product) {
        var updatedProductItemDto = productService.updateProduct(id, product);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProductItemDto);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<ProductItemDto>> getProducts(
            @Valid @ModelAttribute PageRequestDto pageRequestDto) {
        PageRequest pageable = pageRequestDto.toPageable(
                ProductSortField.class, ProductSortField.NAME);

        var pageResponseDto = productService.getProducts(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pageResponseDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductItemDto> getProduct(@PathVariable Long id) {
        var productItemDto = ProductItemDto.from(productService.getProductById(id));
        return ResponseEntity.status(HttpStatus.OK).body(productItemDto);
    }
}
