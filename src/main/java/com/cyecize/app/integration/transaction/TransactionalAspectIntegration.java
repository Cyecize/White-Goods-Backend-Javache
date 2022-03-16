package com.cyecize.app.integration.transaction;

import com.cyecize.ioc.annotations.Service;
import com.cyecize.ioc.handlers.MethodInvocationChain;
import com.cyecize.ioc.handlers.ServiceMethodAspectHandler;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionalAspectIntegration implements ServiceMethodAspectHandler<Transactional> {

    private final TransactionContext transactionContext;

    @Override
    public Object proceed(Transactional transactional,
                          Method method, Object[] objects,
                          MethodInvocationChain methodInvocationChain) throws Exception {
        Optional<String> newTransactionId = Optional.empty();
        Optional<Transaction> transaction = this.transactionContext.getTransaction();

        if (transaction.isEmpty() || transactional.requiresNew()) {
            transaction = Optional.of(this.transactionContext.createTransaction());
            newTransactionId = Optional.of(transaction.get().getTransactionId());
        }

        try {
            final Object invocationResult = methodInvocationChain.proceed();
            newTransactionId.ifPresent(this.transactionContext::commitTransaction);

            return invocationResult;
        } catch (Exception ex) {
            newTransactionId.ifPresent(this.transactionContext::rollbackTransaction);
            throw ex;
        }
    }
}
