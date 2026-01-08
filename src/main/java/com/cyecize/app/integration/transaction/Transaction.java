package com.cyecize.app.integration.transaction;

import javax.persistence.EntityManager;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Transaction {

    private final String transactionId;

    private final EntityManager entityManager;
}
