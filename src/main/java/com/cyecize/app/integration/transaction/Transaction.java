package com.cyecize.app.integration.transaction;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@Data
@RequiredArgsConstructor
public class Transaction {
    private final String transactionId;

    private final EntityManager entityManager;
}
