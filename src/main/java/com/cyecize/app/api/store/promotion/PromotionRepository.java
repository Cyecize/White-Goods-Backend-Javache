package com.cyecize.app.api.store.promotion;

import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.constants.General;
import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public Promotion persist(Promotion promotion) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(promotion);
        return promotion;
    }

    @Transactional
    public List<Promotion> findAllFetchItems() {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager
                .createQuery("select p from Promotion p", Promotion.class)
                .setHint(
                        General.HIBERNATE_HINT_ENTITY_GRAPH,
                        entityManager.getEntityGraph(EntityGraphs.PROMOTION_WITH_ITEMS)
                )
                .getResultList();
    }

    @Transactional
    public void delete(Promotion promo) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.createQuery("delete from Promotion where id = ?1")
                .setParameter(1, promo.getId())
                .executeUpdate();
    }

    @Transactional
    public void merge(Promotion promotion) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.merge(promotion);
    }

    @Transactional
    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }

        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                        "select case when (count(p) > 0)  then true else false end from Promotion p where p.id = ?1",
                        Boolean.class
                ).setParameter(1, id)
                .getSingleResult();


    }
}
