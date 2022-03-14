package com.cyecize.app.api.carousel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "home_carousel")
@Getter
@Setter
@ToString
public class HomeCarousel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;

    private Long productId;

    private String textEn;

    private String textBg;

    private String imageUrl;

    private String customLink;

    private Boolean customLinkSamePage;

    private Boolean enabled;

    private Integer orderNumber;
}
