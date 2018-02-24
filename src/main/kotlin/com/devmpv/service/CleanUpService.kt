package com.devmpv.service

import com.devmpv.model.Attachment
import com.devmpv.repositories.AttachmentRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service to delete obsolete attachments. Running on a schedule defined by
 * **chan.cleanupCron** setting.
 *
 * @author devmpv
 */
@Service
class CleanUpService(private val attachSvc: AttachmentService, private val attachRepo: AttachmentRepository) {

    @Scheduled(cron = "\${chan.cleanupCron}")
    @Transactional
    fun attachmentCleanup() {
        val orphanes = attachRepo.findEmpty()
        if (!orphanes.isEmpty()) {
            for (attach in orphanes) {
                attachSvc.cleanup(attach)
                attachRepo.delete(attach)
            }
            LOG.info(ATTACHMENTS_DELETED, orphanes.size)
        }
    }

    companion object {

        private val ATTACHMENTS_DELETED = "Successfully deleted (%d) orphane attachments"
        private val LOG = LoggerFactory.getLogger(CleanUpService::class.java)
    }
}
