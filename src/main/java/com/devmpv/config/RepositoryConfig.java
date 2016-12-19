package com.devmpv.config;

import com.devmpv.model.Board;
import com.devmpv.model.Message;
import com.devmpv.model.Thread;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 * REST repositories configuration
 * Created by devmpv on 15.12.16.
 */
@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Board.class, Thread.class, Message.class);
	}
}
