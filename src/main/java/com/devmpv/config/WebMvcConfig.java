package com.devmpv.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * WebMVC configuration such as resource folders
 *
 * @author devmpv
 */
@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(WebMvcConfig.class);
    private static final String RESOURCE_FOLDER_MESSAGE = "Adding resource folder from filesystem: %s";

    @Value("${chan.file.path}")
    private String filestorage;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path storagePath = Paths.get(filestorage.replaceFirst("^~", System.getProperty("user.home")));
        String location = "file:" + storagePath.toString() + "/";
        LOG.info(RESOURCE_FOLDER_MESSAGE, location);
        registry.addResourceHandler("/src/attach/**").addResourceLocations(location);
        super.addResourceHandlers(registry);
    }
}