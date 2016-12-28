package com.devmpv.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(WebMvcConfig.class);

	@Value("${chan.file.path}")
	private String filestorage;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path storagePath = Paths.get(filestorage.replaceFirst("^~", System.getProperty("user.home")));
		String location = "file:" + storagePath.toString() + "/";
		LOG.info("Adding resource folder from filesystem: " + location);
		registry.addResourceHandler("/src/attach/**").addResourceLocations(location);
		super.addResourceHandlers(registry);
	}

}