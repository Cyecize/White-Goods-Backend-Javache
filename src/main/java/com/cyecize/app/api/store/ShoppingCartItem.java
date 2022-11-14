package com.cyecize.app.api.store;

import com.cyecize.app.api.product.Product;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "shopping_cart_items")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItem implements Serializable {

    @Id
    @JoinColumn(nullable = false, name = "cartId")
    @ManyToOne(targetEntity = ShoppingCart.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Long cartId;

    @Id
    @JoinColumn(nullable = false, name = "productId")
    @ManyToOne(targetEntity = Product.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;
}
