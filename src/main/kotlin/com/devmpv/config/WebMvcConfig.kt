package com.devmpv.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.nio.file.Paths

/**
 * WebMVC configuration such as resource folders
 *
 * @author devmpv
 */
@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration::class)
class WebMvcConfig : WebMvcConfigurerAdapter() {

    @Value("\${chan.file.path}")
    private val filestorage: String? = null

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val storagePath = Paths.get(filestorage!!.replaceFirst("^~".toRegex(), System.getProperty("user.home")))
        val location = "file:" + storagePath.toString() + "/"
        LOG.info(RESOURCE_FOLDER_MESSAGE, location)
        registry.addResourceHandler("/src/attach/**").addResourceLocations(location)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(WebMvcConfig::class.java)
        private val RESOURCE_FOLDER_MESSAGE = "Adding resource folder from filesystem: %s"
    }
}