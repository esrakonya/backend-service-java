package org.esrakonya.backend.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    // We will add custom TTL (Time-To-Live) logic here later
}
