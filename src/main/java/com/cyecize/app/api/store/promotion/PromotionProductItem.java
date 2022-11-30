package com.cyecize.app.api.store.promotion;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "promotion_product_items")
@Getter
@Setter
@ToString
public class PromotionProductItem implements Serializable {

    @Id
    @Column(nullable = false)
    private Long productId;

    @Id
    @Column(nullable = false)
    private Long promotionId;

    @Column(nullable = false)
    private Integer minQuantity;
}
