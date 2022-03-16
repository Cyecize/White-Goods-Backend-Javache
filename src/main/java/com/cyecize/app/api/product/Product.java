package com.cyecize.app.api.product;

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
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@NamedEntityGraph(name = EntityGraphs.PRODUCT_ALL, includeAllAttributes = true)
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
    private List<Tag> tags;

    private Boolean enabled;

//    /**
//     * @var Image[]
//     * @ORM\OneToMany(targetEntity="App\Entity\Image", mappedBy="product", fetch="LAZY")
//     */
//    private $images;
}
