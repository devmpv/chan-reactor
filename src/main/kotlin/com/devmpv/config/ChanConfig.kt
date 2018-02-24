package com.devmpv.config

import org.apache.http.client.HttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

import net.sf.ehcache.CacheManager

/**
 * Global application configuration.
 *
 * @author devmpv
 */
@Configuration
@EnableScheduling
class ChanConfig {

    @Bean
    fun cacheManager(): CacheManager {
        return CacheManager.create()
    }

    @Bean
    fun httpClient(): HttpClient {
        return HttpClientBuilder.create().build()
    }
}
