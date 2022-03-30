package com.cyecize.app.api.product.productspec;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "specifications")
@Getter
@Setter
@ToString
public class ProductSpecification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;

    private Long specificationTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specificationTypeId", insertable = false, updatable = false)
    @ToString.Exclude
    private SpecificationType specificationType;

    private String valueBg;

    private String valueEn;
}
