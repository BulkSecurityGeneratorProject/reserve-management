package com.skyrider26.reserve.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.skyrider26.reserve.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.Customer.class.getName(), jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.Customer.class.getName() + ".reserves", jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.Customer.class.getName() + ".shippingaddresses", jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.Admin.class.getName(), jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.Reserve.class.getName(), jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.Reserve.class.getName() + ".reservelines", jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.Product.class.getName(), jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.Product.class.getName() + ".categories", jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.ReserveLine.class.getName(), jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.Category.class.getName(), jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.PaymentMethod.class.getName(), jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.ShippingMethod.class.getName(), jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.ShippingAddress.class.getName(), jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.Customer.class.getName() + ".shoppingcarts", jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.Admin.class.getName() + ".reserves", jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.Product.class.getName() + ".reservelines", jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.ShoppingCart.class.getName(), jcacheConfiguration);
            cm.createCache(com.skyrider26.reserve.domain.ShoppingCart.class.getName() + ".reservelines", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
