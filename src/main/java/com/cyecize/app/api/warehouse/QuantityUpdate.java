package com.cyecize.app.api.warehouse;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "quantity_updates")
@Getter
@Setter
@ToString
public class QuantityUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;

    private long productId;

    private Long deliveryId;

    private Long orderId;

    @Enumerated(EnumType.STRING)
    private QuantityUpdateType updateType;

    private LocalDateTime date;

    private int quantityValue;
}
