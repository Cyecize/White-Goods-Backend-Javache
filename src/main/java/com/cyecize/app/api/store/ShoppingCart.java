package com.cyecize.app.api.store;

import com.cyecize.app.constants.EntityGraphs;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "shopping_carts")
@Getter
@Setter
@ToString
@NamedEntityGraph(name = EntityGraphs.SHOPPING_CART_WITH_ITEMS, attributeNodes = {
        @NamedAttributeNode("items")
})
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, insertable = false, updatable = false)
    private Long id;

    private Long userId;

    @OneToMany(targetEntity = ShoppingCartItem.class, mappedBy = "cartId")
    @ToString.Exclude
    private List<ShoppingCartItem> items;

    private LocalDateTime lastModified;
}
