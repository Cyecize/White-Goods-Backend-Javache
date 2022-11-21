package com.cyecize.app.api.store.order;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@ToString
public class OrderItem implements Serializable {
    @Id
    private Long orderId;

    @Id
    private Long productId;

    private Integer quantity;

    private Double priceSnapshot;
}
