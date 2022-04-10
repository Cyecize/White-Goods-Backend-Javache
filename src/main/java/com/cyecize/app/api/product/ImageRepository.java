package com.cyecize.app.api.product;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.Collection;

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
}
