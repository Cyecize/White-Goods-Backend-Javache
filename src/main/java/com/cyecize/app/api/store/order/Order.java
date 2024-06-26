package com.cyecize.app.api.store.order;

import com.cyecize.app.api.store.delivery.DeliveryAddress;
import com.cyecize.app.constants.EntityGraphs;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@NamedEntityGraph(name = EntityGraphs.ORDER_ALL, includeAllAttributes = true)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Double deliveryPrice;

    private Double totalDiscounts;

    private Double subtotal;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime date;

    private LocalDateTime dateCompleted;

    private String trackingNumber;

    private Long addressId;

    private String couponCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addressId", insertable = false, updatable = false)
    @ToString.Exclude
    private DeliveryAddress address;

    @OneToMany(mappedBy = "orderId", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<OrderItem> items;
}
