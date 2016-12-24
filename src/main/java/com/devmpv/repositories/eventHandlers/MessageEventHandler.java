package com.devmpv.repositories.eventHandlers;

import com.devmpv.model.Message;
import com.devmpv.model.Thread;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * Additional thread actions
 * Created by devmpv on 24.12.16.
 */
@Component
@RepositoryEventHandler({Message.class, Thread.class})
public class MessageEventHandler {
	@HandleBeforeCreate
	public void beforeMessageCreate(Thread thread) {
		thread.setUpdated(System.currentTimeMillis());
	}
}
