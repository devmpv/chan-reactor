package com.devmpv.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Configuraton of websocket message broker.
 * 
 * @author devmpv
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    public static final String MESSAGE_PREFIX = "/topic";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
	registry.enableSimpleBroker(MESSAGE_PREFIX);
	registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
	registry.addEndpoint("/chan").withSockJS();
    }
}
