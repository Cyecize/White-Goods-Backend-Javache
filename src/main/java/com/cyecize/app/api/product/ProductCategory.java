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
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "product_categories")
@Getter
@Setter
@ToString
@NamedEntityGraph(name = EntityGraphs.PRODUCT_CATEGORY_ALL, includeAllAttributes = true)
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;

    private String nameEn;

    private String nameBg;

    private String imageUrl;

    @ManyToMany(targetEntity = Tag.class, fetch = FetchType.LAZY)
    @JoinTable(name = "product_category_tags",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ToString.Exclude
    private List<Tag> tags;
}
