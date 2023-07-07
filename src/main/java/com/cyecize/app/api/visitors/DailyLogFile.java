package com.cyecize.app.api.visitors;

import com.cyecize.app.api.visitors.dailyvisitordata.VisitorHitsPerHour;
import com.cyecize.app.api.visitors.dailyvisitordata.VisitorHitsPerIp;
import com.cyecize.app.api.visitors.dailyvisitordata.VisitorHitsPerPage;
import com.cyecize.app.constants.EntityGraphs;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "daily_log_files")
@Getter
@Setter
@ToString
@NamedEntityGraph(name = EntityGraphs.DAILY_LOG_FILE_ALL, includeAllAttributes = true)
public class DailyLogFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;

    private String filename;

    private LocalDateTime dateProcessed;

    private Integer uniqueVisitors;

    @ToString.Exclude
    @OneToMany(mappedBy = "dailyLogFile")
    private List<VisitorHitsPerHour> hitsPerHour;

    @ToString.Exclude
    @OneToMany(mappedBy = "dailyLogFile")
    private Set<VisitorHitsPerIp> hitsPerIp;

    @ToString.Exclude
    @OneToMany(mappedBy = "dailyLogFile")
    private Set<VisitorHitsPerPage> hitsPerPage;
}
