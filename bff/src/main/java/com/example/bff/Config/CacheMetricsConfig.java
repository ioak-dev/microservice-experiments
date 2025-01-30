package com.example.bff.Config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import io.micrometer.core.instrument.MeterRegistry;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheMetricsConfig {

  @Bean
  public CacheManager cacheManager(MeterRegistry meterRegistry) {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("userCache", "productCache", "cartCache");
    cacheManager.setCaffeine(Caffeine.newBuilder()
        .recordStats()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .maximumSize(100));

    cacheManager.getCacheNames().forEach(name -> {
      Cache cache = cacheManager.getCache(name);
      if (cache instanceof CaffeineCache) {
        CaffeineCache caffeineCache = (CaffeineCache) cache;
        CacheStats stats = caffeineCache.getNativeCache().stats();
        meterRegistry.gauge(name + "_cache_hits", stats, CacheStats::hitCount);
        meterRegistry.gauge(name + "_cache_misses", stats, CacheStats::missCount);
      }
    });

    return cacheManager;
  }
}
