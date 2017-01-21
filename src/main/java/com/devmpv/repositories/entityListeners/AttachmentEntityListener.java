package com.devmpv.repositories.entityListeners;

import javax.annotation.PostConstruct;
import javax.persistence.PostRemove;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.devmpv.model.Attachment;
import com.devmpv.service.AttachmentService;

@Component
public class AttachmentEntityListener {

	private static final Logger LOG = LoggerFactory.getLogger(AttachmentEntityListener.class);
	private static AttachmentService attachSvc;

	@PostConstruct
	public void init() {
		LOG.info("Initialising service for listener: [" + attachSvc + "]");
	}

	@PostRemove
	public void onPostRemove(Attachment attachment) {
		attachSvc.cleanup(attachment);
	}

	@Autowired(required = true)
	public void setRepository(AttachmentService attachmentService) {
		attachSvc = attachmentService;
	}
}
