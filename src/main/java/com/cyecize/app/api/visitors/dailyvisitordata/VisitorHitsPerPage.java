package com.cyecize.app.api.visitors.dailyvisitordata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "visitor_hits_per_page")
@Getter
@Setter
@ToString
public class VisitorHitsPerPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;

    private Long dailyLogFileId;

    private String url;

    private Integer hits;
}
