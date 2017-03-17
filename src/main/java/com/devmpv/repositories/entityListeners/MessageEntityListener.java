package com.devmpv.repositories.entityListeners;

import static com.devmpv.config.WebSocketConfig.MESSAGE_PREFIX;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.PostPersist;
import javax.persistence.PreRemove;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.devmpv.model.Message;
import com.devmpv.model.Thread;
import com.devmpv.repositories.AttachmentRepository;

@Component
public class MessageEntityListener {

	private static final Logger LOG = LoggerFactory.getLogger(MessageEntityListener.class);
	private static AttachmentRepository attachRepo;
	private static SimpMessagingTemplate template;
	private static EntityLinks entityLinks;

	private String getPath(Message message) {
		return entityLinks.linkForSingleResource(message.getClass(), message.getId()).toUri().getPath();
	}

	@PostConstruct
	public void init() {
		LOG.info("Initialising repository for listener: [" + attachRepo + "]");
	}

	@PostPersist
	public void onPostPersist(Message message) {
		if (message instanceof Thread) {
			return;
		}
		Map<String, Object> headers = new HashMap<>();
		headers.put("thread", message.getThread().getId());
		template.convertAndSend(MESSAGE_PREFIX + "/newMessage", getPath(message), headers);
	}

	@PreRemove
	public void onPreRemove(Message message) {
		message.getAttachments().forEach(attach -> {
			if (attach.getMessages().size() == 1 && attach.getMessages().contains(message)) {
				attachRepo.delete(attach);
			}
		});
	}

	@Autowired(required = true)
	public void setRepository(AttachmentRepository attachmentRepository, SimpMessagingTemplate websocket,
			EntityLinks links) {
		attachRepo = attachmentRepository;
		template = websocket;
		entityLinks = links;
	}
}
