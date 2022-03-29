package com.cyecize.app.api.product;

import com.cyecize.app.api.product.productspec.Specification;
import com.cyecize.app.constants.EntityGraphs;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@NamedEntityGraph(name = EntityGraphs.PRODUCT_ALL, attributeNodes = {
        @NamedAttributeNode("tags"),
        @NamedAttributeNode(value = "specifications", subgraph = "specificationTypes"),
        @NamedAttributeNode("category"),
        @NamedAttributeNode("images"),
},subgraphs = {
        @NamedSubgraph(name = "specificationTypes", attributeNodes = @NamedAttributeNode("specificationType"))
})
@NamedEntityGraph(name = EntityGraphs.PRODUCT_FOR_SEARCH, attributeNodes = {
        @NamedAttributeNode("tags")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;

    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "categoryId", insertable = false, updatable = false)
    private ProductCategory category;

    private String productName;

    private Double price;

    @Column(length = 65535, columnDefinition = "TEXT")
    private String descriptionBg;

    @Column(length = 65535, columnDefinition = "TEXT")
    private String descriptionEn;

    private String imageUrl;

    @ManyToMany(targetEntity = Tag.class, fetch = FetchType.LAZY)
    @JoinTable(name = "products_tags",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ToString.Exclude
    private Set<Tag> tags;

    private Boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(name = "products_specifications",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "specification_id")
    )
    private Set<Specification> specifications;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @ToString.Exclude
    private Set<Image> images;
}
