package com.cyecize.app.config;

import com.cyecize.app.api.store.ShoppingCartService;
import com.cyecize.summer.common.annotations.Component;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.PostConstruct;
import com.cyecize.summer.common.annotations.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShoppingSessionCleanerCronJob {

    private final ShoppingCartService shoppingCartService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Configuration("shopping.cart.session.scan.interval.hours")
    private final int scanIntervalHours;

    @PostConstruct
    public void init() {
        this.scheduler.scheduleAtFixedRate(
                this::cleanCarts,
                this.scanIntervalHours,
                this.scanIntervalHours,
                TimeUnit.HOURS
        );
    }

    @PreDestroy
    public void destroy() {
        this.scheduler.shutdown();
    }

    private void cleanCarts() {
        this.shoppingCartService.removeExpiredSessions();
    }
}
