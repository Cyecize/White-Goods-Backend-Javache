package com.cyecize.app.api.store.order.dto;

import com.cyecize.app.api.product.dto.ProductDto;
import lombok.Data;

@Data
public class OrderItemDto {

    private Long orderId;

    private Long productId;

    private ProductDto product;

    private Integer quantity;

    private Double priceSnapshot;
}
