package com.cyecize.app.api.store.order.dto;

import com.cyecize.app.api.product.dto.ProductDto;
import com.cyecize.app.util.MathUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderItemDto {

    private Long orderId;

    private Long productId;

    private ProductDto product;

    private Integer quantity;

    private Double priceSnapshot;

    @JsonProperty("calculatedPrice")
    public Double getCalculatedPrice() {
        return MathUtil.calculatePrice(this.priceSnapshot, this.quantity);
    }
}
