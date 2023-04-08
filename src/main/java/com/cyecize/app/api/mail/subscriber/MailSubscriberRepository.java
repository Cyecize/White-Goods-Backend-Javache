package com.cyecize.app.api.mail.subscriber;

import com.cyecize.app.api.mail.MailSubscriberType;
import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailSubscriberRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public List<String> selectEmailWhereTypeEquals(MailSubscriberType type) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();

        return entityManager.createQuery(
                        "select s.email from MailSubscriber s where s.subscriberType = :subType",
                        String.class
                )
                .setParameter("subType", type)
                .getResultList();
    }
}
