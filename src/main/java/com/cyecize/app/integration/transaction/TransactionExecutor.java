package com.cyecize.app.integration.transaction;

import com.cyecize.summer.common.annotations.Component;

@Component
public class TransactionExecutor {

    @Transactional
    public void execute(Runnable runnable) {
        runnable.run();
    }

}
