package com.cyecize.app.config.db;

import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.PHYSICAL_NAMING_STRATEGY;
import static org.hibernate.cfg.AvailableSettings.SHOW_SQL;

import com.cyecize.solet.SoletConstants;
import com.cyecize.summer.common.annotations.Bean;
import com.cyecize.summer.common.annotations.BeanConfig;
import com.cyecize.summer.common.annotations.Configuration;
import java.util.HashMap;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.HibernatePersistenceProvider;

@BeanConfig
@RequiredArgsConstructor
public class EntityManagerFactoryBeanConfig {

    @Configuration("persistence.unit.name")
    private final String persistenceUnitName;

    @Configuration("database.show.sql")
    private final boolean showSql;

    @Configuration("database.dialect")
    private final String databaseDialect;

    @Configuration("database.naming.strategy")
    private final String namingStrategy;

    private final DataSource dataSource;

    @Configuration(SoletConstants.SOLET_CFG_WORKING_DIR)
    private final String workingDir;

    // Inject so that flyway runs before creating hibernate
    private final FlywayConfig flywayConfig;

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        final Properties properties = new Properties();
        properties.put(DIALECT, this.databaseDialect);
        properties.put(SHOW_SQL, this.showSql);
        properties.put(PHYSICAL_NAMING_STRATEGY, this.namingStrategy);

        return new HibernatePersistenceProvider().createContainerEntityManagerFactory(
                new PersistenceUnitInfoImpl(
                        this.persistenceUnitName,
                        dataSource,
                        properties,
                        this.workingDir
                ),
                new HashMap<>()
        );
    }
}
