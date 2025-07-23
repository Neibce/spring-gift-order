package gift.product.option.controller;

import gift.product.option.dto.OptionCreateRequestDto;
import gift.product.option.dto.OptionItemDto;
import gift.product.option.service.OptionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping
    public ResponseEntity<OptionItemDto> createOption(
            @PathVariable Long productId,
            @Valid @RequestBody OptionCreateRequestDto optionCreateRequestDto) {
        var createdOptionItemDto = optionService.createOption(productId, optionCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOptionItemDto);
    }

    @GetMapping
    public ResponseEntity<List<OptionItemDto>> getOptions(@PathVariable Long productId) {
        var optionItemDtos = optionService.getProductOptions(productId);
        return ResponseEntity.status(HttpStatus.OK).body(optionItemDtos);
    }

    @DeleteMapping("{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
