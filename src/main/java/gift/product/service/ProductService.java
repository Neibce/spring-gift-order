package gift.product.service;

import gift.common.dto.PageResponseDto;
import gift.exception.EntityNotFoundException;
import gift.exception.MdApprovalRequiredException;
import gift.product.dto.ProductCreateRequestDto;
import gift.product.dto.ProductItemDto;
import gift.product.dto.ProductUpdateRequestDto;
import gift.product.entity.Product;
import gift.product.option.dto.OptionCreateRequestDto;
import gift.product.option.service.OptionService;
import gift.product.repository.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private static final String PRODUCT_NOT_FOUND_MESSAGE = "해당 상품을 찾을 수 없습니다.";
    private final ProductRepository productRepository;
    private final OptionService optionService;

    public ProductService(ProductRepository productRepository, OptionService optionService) {
        this.productRepository = productRepository;
        this.optionService = optionService;
    }

    public ProductItemDto createProduct(ProductCreateRequestDto requestDto) {
        checkRestrictedWords(requestDto.name());

        Product newProduct = productRepository.save(new Product(requestDto));

        OptionCreateRequestDto optionCreateRequestDto = OptionCreateRequestDto.from(requestDto.name(), 1);
        optionService.createOption(newProduct.getId(), optionCreateRequestDto);

        return ProductItemDto.from(newProduct);
    }

    @Transactional
    public ProductItemDto updateProduct(Long id, ProductUpdateRequestDto requestDto) {
        checkRestrictedWords(requestDto.name());

        Product product = getProductById(id);
        product.update(requestDto);
        return ProductItemDto.from(product);
    }

    private void checkRestrictedWords(String name) {
        if (name.contains("카카오")) {
            throw new MdApprovalRequiredException("'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
        }
    }

    @Transactional
    public void deleteProduct(Long id) {
        validateProductExists(id);
        productRepository.deleteById(id);
    }

    public List<ProductItemDto> getProducts() {
        return productRepository.findAll().stream()
                .map(ProductItemDto::from)
                .toList();
    }

    public PageResponseDto<ProductItemDto> getProducts(Pageable pageable) {
        return PageResponseDto.from(
                productRepository.findAll(pageable).map(ProductItemDto::from)
        );
    }

    public Product getProductById(Long id) throws EntityNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_MESSAGE));
    }

    public void validateProductExists(Long id) throws EntityNotFoundException {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException(PRODUCT_NOT_FOUND_MESSAGE);
        }
    }
}
