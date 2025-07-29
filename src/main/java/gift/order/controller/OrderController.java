package gift.order.controller;

import gift.annotation.LoginMember;
import gift.member.entity.Member;
import gift.order.dto.OrderInfoDto;
import gift.order.dto.OrderRequestDto;
import gift.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderInfoDto> createOrder(
            @LoginMember Member member,
            @Valid @RequestBody OrderRequestDto requestDto) {
        var createdOrderInfoDto = orderService.createOrder(member, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderInfoDto);
    }

}
