package com.devmpv.config;

import com.devmpv.model.BoardRepository;
import com.devmpv.service.DatabaseLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;


import java.util.Properties;

/**
 * Configuration
 * Created by devmpv on 09.12.16.
 */
@Configuration
public class ChanConfig {

   /* @Bean
    public SimpleUrlHandlerMapping handlerMapping(@Autowired BoardRepository boardRepo){
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Integer.MAX_VALUE - 2);
        Properties props = new Properties();
        boardRepo.findAll().forEach(board -> props.put(board.getId(), "homeController"));
        props.put("b", "homeController");
        props.put("po", "homeController");
        mapping.setMappings(props);
        return mapping;
    }*/
}
