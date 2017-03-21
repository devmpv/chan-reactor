package com.devmpv.service;

import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.devmpv.model.Attachment;
import com.devmpv.repositories.AttachmentRepository;

@Service
public class CleanUpService {
	private static final String ATTACHMENTS_DELETED = "Successfully deleted (%d) orphane attachments";
	private static final Logger LOG = LoggerFactory.getLogger(CleanUpService.class);

	@Autowired
	private AttachmentRepository attachRepo;

	@Autowired
	private AttachmentService attachSvc;

	@Scheduled(cron = "${chan.cleanupCron}")
	@Transactional
	public void AttachmentCleanup() throws Exception {
		Set<Attachment> orphanes = attachRepo.findEmpty();
		if (orphanes.size() > 0) {
			for (Attachment attach : orphanes) {
				attachSvc.cleanup(attach);
				attachRepo.delete(attach);
			}
			LOG.info(String.format(ATTACHMENTS_DELETED, orphanes.size()));
		}
	}
}
