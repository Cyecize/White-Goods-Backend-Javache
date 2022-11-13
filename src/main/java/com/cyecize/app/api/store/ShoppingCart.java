package com.cyecize.app.api.store;

import com.cyecize.app.api.user.User;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class ShoppingCart {

    @Id
    @Column(nullable = false, insertable = false, updatable = false)
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(nullable = false, name = "userId")
    private Long userId;

    @OneToMany(targetEntity = ShoppingCartItem.class, mappedBy = "cartId")
    private List<ShoppingCartItem> items;

    private LocalDateTime lastModified;
}
