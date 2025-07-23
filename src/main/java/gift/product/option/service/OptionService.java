package gift.product.option.service;

import gift.exception.EntityAlreadyExistsException;
import gift.exception.EntityNotFoundException;
import gift.product.entity.Product;
import gift.product.option.dto.OptionCreateRequestDto;
import gift.product.option.dto.OptionItemDto;
import gift.product.option.entity.Option;
import gift.product.option.repository.OptionRepository;
import gift.product.service.ProductService;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductService productService;

    public OptionService(OptionRepository optionRepository, @Lazy ProductService productService) {
        this.optionRepository = optionRepository;
        this.productService = productService;
    }

    @Transactional
    public OptionItemDto createOption(Long productId, OptionCreateRequestDto requestDto) {
        Product product = productService.getProductById(productId);
        Option newOption = new Option(product, requestDto);

        if (optionRepository.existsByNameAndProductId(newOption.getName(), productId)) {
            throw new EntityAlreadyExistsException("이미 존재하는 옵션입니다.");
        }
        return OptionItemDto.of(optionRepository.save(newOption));
    }

    @Transactional(readOnly = true)
    public List<OptionItemDto> getProductOptions(Long productId) {
        productService.validateProductExists(productId);
        return optionRepository.getOptionsByProductId(productId)
                .stream().map(OptionItemDto::of).toList();
    }

    @Transactional
    public void deleteOption(Long optionId) {
        Option option = getOptionById(optionId);

        if(optionRepository.countByProductId(option.getProduct().getId()) == 1) {
            throw new EntityNotFoundException("최소 하나의 옵션은 남아 있어야 합니다.");
        }
        optionRepository.deleteById(optionId);
    }

    @Transactional
    public void subtractOptionQuantity(Long optionId, int quantity) {
        Option option = getOptionById(optionId);
        option.subtract(quantity);
    }

    private Option getOptionById(Long optionId) {
        return optionRepository.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("옵션을 찾을 수 없습니다."));
    }
}
