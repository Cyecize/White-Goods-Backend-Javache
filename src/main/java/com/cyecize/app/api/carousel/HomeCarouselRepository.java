package com.cyecize.app.api.carousel;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class HomeCarouselRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public HomeCarousel persist(HomeCarousel homeCarousel) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(homeCarousel);

        return homeCarousel;
    }
}
