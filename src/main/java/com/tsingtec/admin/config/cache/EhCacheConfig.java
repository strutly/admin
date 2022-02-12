package com.tsingtec.admin.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Configuration
public class EhCacheConfig {

    /**
     * ehcache缓存管理器；shiro整合ehcache：
     * 通过安全管理器：securityManager
     * @return EhCacheManager
     */
    @Order(1)
    @Bean(name="ehCacheManager")
    public EhCacheManager ehCacheManager() {
        log.error("=====shiro整合ehcache缓存：EhCacheConfig.ehCacheManager()");
        EhCacheManager ehcache = new EhCacheManager();
        ehcache.setCacheManager(ehCacheManagerFactoryBean().getObject());
        return ehcache;
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }

}