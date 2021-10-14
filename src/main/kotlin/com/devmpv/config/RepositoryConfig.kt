package com.devmpv.config

import org.springframework.context.annotation.Configuration
import com.devmpv.model.Board
import com.devmpv.model.Message
import com.devmpv.model.Thread
import org.springframework.context.annotation.Bean
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer

/**
 * REST repositories configuration
 *
 * @author devmpv
 */
@Configuration
class RepositoryConfig {

    @Bean
    fun repositoryRestConfigurer(): RepositoryRestConfigurer =
        RepositoryRestConfigurer.withConfig { it ->
            it.exposeIdsFor(Board::class.java, Thread::class.java, Message::class.java)
        }
}
