package gift.order.service;

import gift.exception.EntityNotFoundException;
import gift.member.entity.Member;
import gift.member.enums.AuthProvider;
import gift.oauth.kakao.service.KakaoOAuthService;
import gift.order.dto.KakaoSendMessageResponseDto;
import gift.order.dto.OrderInfoDto;
import gift.order.dto.OrderRequestDto;
import gift.order.entity.Order;
import gift.order.repository.OrderRepository;
import gift.product.option.entity.Option;
import gift.product.option.service.OptionService;
import gift.wishlist.service.WishlistService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final WishlistService wishlistService;
    private final KakaoOAuthService kakaoOAuthService;
    private final RestTemplate restTemplate;

    public OrderService(OrderRepository orderRepository, OptionService optionService,
            WishlistService wishlistService, KakaoOAuthService kakaoOAuthService,
            RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishlistService = wishlistService;
        this.kakaoOAuthService = kakaoOAuthService;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public OrderInfoDto createOrder(Member member, OrderRequestDto requestDto) {
        Option option = optionService.getOptionById(requestDto.optionId());
        if (option.getQuantity() < requestDto.quantity()) {
            throw new EntityNotFoundException("재고가 부족합니다.");
        }

        Order order = new Order(member, option, requestDto.quantity(), requestDto.message());
        option.subtract(requestDto.quantity());
        wishlistService.deleteIfExists(member, option.getId());

        orderRepository.save(order);
        sendKakaoTalkMessage(member, order);
        return OrderInfoDto.from(order);
    }

    private void sendKakaoTalkMessage(Member member, Order order) {
        if (member.getAuthProvider() != AuthProvider.KAKAO) {
            return;
        }

        String accessToken = kakaoOAuthService.renewToken(member.getOAuthInfo().getRefreshToken())
                .accessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("object_type", "text");
        body.add("template_object", buildOrderCompletionMessage(order, member));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<KakaoSendMessageResponseDto> response = restTemplate.postForEntity(
                "https://kapi.kakao.com/v2/api/talk/memo/default/send", request,
                KakaoSendMessageResponseDto.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null
                || response.getBody().result_code() != 0) {
            throw new HttpClientErrorException(response.getStatusCode(), "Kakao 요청 실패");
        }
    }

    private String buildOrderCompletionMessage(Order order, Member member) {
        return String.format("""
                            {
                                "object_type": "text",
                                "text": "주문이 완료되었습니다!\\n주문 번호: %s\\n주문자: %s\\n상품명: %s\\n수량: %d\\n메시지: %s\\n주문일시: %s",
                                "link": {
                                    "web_url": "https://naver.com",
                                    "mobile_web_url": "https://naver.com"
                                },
                                "button_title": "주문 내역 보기"
                            }
                        """, order.getId(), member.getName(), order.getOption().getName(),
                order.getQuantity(), order.getMessage(), order.getCreatedAt());
    }
}
