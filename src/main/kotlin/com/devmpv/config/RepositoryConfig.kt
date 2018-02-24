package com.devmpv.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter

import com.devmpv.model.Board
import com.devmpv.model.Message
import com.devmpv.model.Thread

/**
 * REST repositories configuration
 *
 * @author devmpv
 */
@Configuration
class RepositoryConfig : RepositoryRestConfigurerAdapter() {

    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration?) {
        config!!.exposeIdsFor(Board::class.java, Thread::class.java, Message::class.java)
    }
}
