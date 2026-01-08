package com.cyecize.app.integration.transaction;

import com.cyecize.app.error.ApiException;
import com.cyecize.summer.common.annotations.Component;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionContext {

    private static final ThreadLocal<Stack<Transaction>> transactions = new ThreadLocal<>();

    private final EntityManagerFactory emf;

    public Transaction createTransaction() {
        final EntityManager entityManager = this.emf.createEntityManager();

        entityManager.getTransaction().begin();

        final Transaction transaction = new Transaction(
                UUID.randomUUID().toString(),
                entityManager
        );

        this.getTransactions().push(transaction);

        return transaction;
    }

    public void commitTransaction(String transactionId) {
        final Transaction transaction = this.findTransaction(transactionId);

        try {
            transaction.getEntityManager().getTransaction().commit();
        } finally {
            transaction.getEntityManager().close();
        }

        this.getTransactions().remove(transaction);
    }

    public void rollbackTransaction(String transactionId) {
        final Transaction transaction = this.findTransaction(transactionId);

        try {
            transaction.getEntityManager().getTransaction().rollback();
        } finally {
            transaction.getEntityManager().close();
        }

        this.getTransactions().remove(transaction);
    }

    public EntityManager getEntityManagerForTransaction() {
        return this.getTransaction()
                .map(Transaction::getEntityManager)
                .orElseThrow(() -> new ApiException(
                        "Cannot obtain EntityManager because there is no transaction."));
    }

    public Optional<Transaction> getTransaction() {
        final Stack<Transaction> transactions = this.getTransactions();
        if (transactions.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(transactions.peek());
    }

    private Transaction findTransaction(String transactionId) {
        return this.getTransactions().stream()
                .filter(t -> t.getTransactionId().equals(transactionId))
                .findFirst()
                .orElseThrow(() -> new ApiException(String.format(
                        "Transaction with id '%s' was not found.", transactionId
                )));
    }

    private Stack<Transaction> getTransactions() {
        if (transactions.get() == null) {
            transactions.set(new Stack<>());
        }

        return transactions.get();
    }
}
