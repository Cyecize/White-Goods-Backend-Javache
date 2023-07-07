package com.cyecize.app.api.visitors;

import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Service;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DailyLogFileRepository {

    private final TransactionContext transactionContext;
    private final SpecificationExecutor specificationExecutor;

    @Transactional
    public DailyLogFile persist(DailyLogFile dailyLogFile) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(dailyLogFile);
        return dailyLogFile;
    }

    @Transactional
    public DailyLogFile findById(Long id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();

        return entityManager.createQuery(
                        "select dlf from DailyLogFile dlf where dlf.id = :id",
                        DailyLogFile.class)
                .setParameter("id", id)
                .getResultStream().findFirst().orElse(null);
    }

    @Transactional
    public DailyLogFile getLatest() {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                        "select dlf from DailyLogFile dlf order by dlf.dateProcessed desc ",
                        DailyLogFile.class)
                .setMaxResults(1)
                .getResultStream().findFirst().orElse(null);
    }

    @Transactional
    public List<DailyLogFile> searchByDateFetchAll(LocalDateTime start, LocalDateTime end) {
        return this.specificationExecutor.findAll(
                DailyLogFileSpecifications.betweenDate(start, end),
                DailyLogFile.class,
                EntityGraphs.DAILY_LOG_FILE_ALL
        );
    }
}
