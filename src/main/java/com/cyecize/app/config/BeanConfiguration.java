package com.cyecize.app.config;

import com.cyecize.summer.common.annotations.Bean;
import com.cyecize.summer.common.annotations.BeanConfig;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@BeanConfig
public class BeanConfiguration {
    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return Persistence.createEntityManagerFactory("whiteGoodsStore");
    }
}
