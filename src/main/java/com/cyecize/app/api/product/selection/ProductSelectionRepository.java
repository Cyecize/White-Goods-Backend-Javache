package com.cyecize.app.api.product.selection;

import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.constants.General;
import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.ioc.annotations.Service;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductSelectionRepository {

    private final TransactionContext transactionContext;
    private final SpecificationExecutor specificationExecutor;

    @Transactional
    public List<ProductSelection> findAll(Specification<ProductSelection> spec, String graphName) {
        return this.specificationExecutor.findAll(spec, ProductSelection.class, graphName);
    }

    @Transactional
    public void save(ProductSelection productSelection) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.merge(productSelection);
    }

    @Transactional
    public ProductSelection findOne(Long id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager
                .createQuery("select ps from ProductSelection ps where ps.id = :id",
                        ProductSelection.class
                ).setParameter("id", id)
                .setHint(
                        General.HIBERNATE_HINT_ENTITY_GRAPH,
                        entityManager.getEntityGraph(EntityGraphs.PRODUCT_SELECTION_FOR_SEARCH)
                )
                .getResultStream()
                .findFirst().orElse(null);
    }

    @Transactional
    public void remove(Long id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.createQuery("delete from ProductSelection where id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
