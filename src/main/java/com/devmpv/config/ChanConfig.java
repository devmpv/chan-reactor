package com.devmpv.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import net.sf.ehcache.CacheManager;

/**
 * Global application configuration.
 * 
 * @author devmpv
 *
 */
@Configuration
@EnableScheduling
public class ChanConfig {

    @Bean
    public CacheManager cacheManager() {
	    return CacheManager.create();
    }

    @Bean
    public HttpClient httpClient() {
	    return HttpClientBuilder.create().build();
    }
}
