package gift.order.entity;

import gift.common.entity.BaseEntity;
import gift.member.entity.Member;
import gift.product.option.entity.Option;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Option option;

    @Column(nullable = false)
    private Integer quantity;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public Order() {

    }

    public Order(Member member, Option option, Integer quantity, String message) {
        this.member = member;
        this.option = option;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Option getOption() {
        return option;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}

