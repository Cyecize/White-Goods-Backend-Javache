package com.cyecize.app.api.product.selection;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.constants.EntityGraphs;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "product_selections")
@Getter
@Setter
@ToString
@NamedEntityGraphs(
        @NamedEntityGraph(name = EntityGraphs.PRODUCT_SELECTION_FOR_SEARCH,
                attributeNodes = {
                        @NamedAttributeNode(value = "product", subgraph = "product-tags")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "product-tags",
                                attributeNodes = {
                                        @NamedAttributeNode("tags")
                                }
                        )
                })

)
public class ProductSelection {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Product product;

    @Column(nullable = false)
    private Long orderNumber;
}
