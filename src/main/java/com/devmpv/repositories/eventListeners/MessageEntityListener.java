package com.devmpv.repositories.eventListeners;

import javax.annotation.PostConstruct;
import javax.persistence.PreRemove;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.devmpv.model.Message;
import com.devmpv.repositories.AttachmentRepository;

@Component
public class MessageEntityListener {

	private static final Logger LOG = LoggerFactory.getLogger(MessageEntityListener.class);
	private static AttachmentRepository attachRepo;

	@PostConstruct
	public void init() {
		LOG.info("Initialising repository for listener: [" + attachRepo + "]");
	}

	@PreRemove
	public void onPreRemove(Message message) {
		message.getAttachments().forEach(attach -> {
			if (attachRepo.countMessages(attach.getId()) == 1) {
				attachRepo.delete(attach);
			}
		});
	}

	@Autowired(required = true)
	public void setRepository(AttachmentRepository attachmentRepository) {
		attachRepo = attachmentRepository;
	}
}
