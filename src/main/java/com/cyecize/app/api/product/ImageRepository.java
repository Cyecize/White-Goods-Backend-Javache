package com.cyecize.app.api.product;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageRepository {
    private final TransactionContext transactionContext;

    @Transactional
    public void persistAll(Collection<Image> images) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        for (Image image : images) {
            entityManager.persist(image);
        }
    }

    @Transactional
    public void delete(Image image) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.createQuery("delete from Image where id = ?1")
                .setParameter(1, image.getId())
                .executeUpdate();
    }

    @Transactional
    public List<Image> findByProductId(Long productId) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery("select i from Image i where i.productId = ?1", Image.class)
                .setParameter(1, productId)
                .getResultList();
    }

    @Transactional
    public Image findById(long imageId) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery("select i from Image i where i.id = ?1", Image.class)
                .setParameter(1, imageId)
                .getResultStream().findFirst().orElse(null);
    }
}
