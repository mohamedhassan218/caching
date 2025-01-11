package com.example.demo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Indicates that this class contains Spring configuration methods, allowing it to define beans.
@EnableCaching // Enables Spring's caching support, activating annotations like @Cacheable.
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // Create a cache manager with a cache named 'employeesCache'.
        // This cache manager integrates with the Caffeine library providing a efficient caching.
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("employeesCache");

        // Returns a builder to configure the Caffeine cache & enable statistics recording for this cache.
        cacheManager.setCaffeine(Caffeine.newBuilder().recordStats());

        // Returns the configured Cache Manager instance to be used across the app.
        return cacheManager;
    }

}
