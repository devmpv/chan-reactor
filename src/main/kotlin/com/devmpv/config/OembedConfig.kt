package com.devmpv.config

import org.apache.http.client.HttpClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import ac.simons.oembed.OembedEndpoint
import ac.simons.oembed.OembedService
import net.sf.ehcache.CacheManager

@Configuration
@ConfigurationProperties(prefix = "chan.oembed")
class OembedConfig {
    var endpoints: List<OembedEndpoint>? = null
    var isAutodiscovery = false
    var cacheName: String? = null
    var defaultCacheAge: Int? = null

    @Bean
    fun oembedService(httpClient: HttpClient, cacheManager: CacheManager): OembedService {

        val oembedService = OembedService(httpClient, cacheManager, endpoints!!, "chan")
        oembedService.isAutodiscovery = this.isAutodiscovery
        if (this.cacheName != null) {
            oembedService.cacheName = cacheName
        }
        if (this.defaultCacheAge != null) {
            oembedService.defaultCacheAge = defaultCacheAge!!.toLong()
        }
        return oembedService
    }
}