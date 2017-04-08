package com.devmpv.config;

import java.util.List;

import org.apache.http.client.HttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ac.simons.oembed.OembedEndpoint;
import ac.simons.oembed.OembedService;
import net.sf.ehcache.CacheManager;

@Configuration
@ConfigurationProperties(prefix = "chan.oembed")
public class OembedConfig {
    private List<OembedEndpoint> endpoints;
    private boolean autodiscovery = false;
    private String cacheName;
    private Integer defaultCacheAge;

    public String getCacheName() {
	return cacheName;
    }

    public Integer getDefaultCacheAge() {
	return defaultCacheAge;
    }

    public List<OembedEndpoint> getEndpoints() {
	return endpoints;
    }

    public boolean isAutodiscovery() {
	return autodiscovery;
    }

    @Bean
    public OembedService oembedService(HttpClient httpClient, CacheManager cacheManager) {
	final OembedService oembedService = new OembedService(httpClient, cacheManager, endpoints, "chan");
	oembedService.setAutodiscovery(this.autodiscovery);
	if (this.cacheName != null) {
	    oembedService.setCacheName(cacheName);
	}
	if (this.defaultCacheAge != null) {
	    oembedService.setDefaultCacheAge(defaultCacheAge);
	}
	return oembedService;
    }

    public void setAutodiscovery(boolean autodiscovery) {
	this.autodiscovery = autodiscovery;
    }

    public void setCacheName(String cacheName) {
	this.cacheName = cacheName;
    }

    public void setDefaultCacheAge(Integer defaultCacheAge) {
	this.defaultCacheAge = defaultCacheAge;
    }

    public void setEndpoints(List<OembedEndpoint> endpoints) {
	this.endpoints = endpoints;
    }
}