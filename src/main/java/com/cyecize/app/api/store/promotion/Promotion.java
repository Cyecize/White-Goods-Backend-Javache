package com.cyecize.app.api.store.promotion;

import com.cyecize.app.constants.EntityGraphs;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "promotions")
@Getter
@Setter
@ToString
@NamedEntityGraph(name = EntityGraphs.PROMOTION_WITH_ITEMS, attributeNodes = {
        @NamedAttributeNode("productItems")
})
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nameBg;

    @Column(nullable = false)
    private String nameEn;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PromotionType promotionType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private Long categoryId;

    private Double discount;

    private Double minSubtotal;

    @OneToMany(
            targetEntity = PromotionProductItem.class,
            fetch = FetchType.LAZY,
            mappedBy = "promotionId")
    private List<PromotionProductItem> productItems;
}
