package com.cyecize.app.config.db;

import com.cyecize.summer.common.annotations.Component;
import com.cyecize.summer.common.annotations.PostConstruct;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;

@Component
@RequiredArgsConstructor
public class FlywayConfig {

    private final DataSource dataSource;

    @PostConstruct
    public void init() {
        Flyway.configure()
                .baselineOnMigrate(true)
                .baselineVersion("0.1")
                .dataSource(this.dataSource)
                .locations("migrations/")
                .load()
                .migrate();
    }
}
