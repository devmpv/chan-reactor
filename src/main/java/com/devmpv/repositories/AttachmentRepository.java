package com.devmpv.repositories;

import java.io.Serializable;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.devmpv.model.Attachment;

/**
 * Attachment repository.
 * 
 * @author devmpv
 *
 */
public interface AttachmentRepository extends CrudRepository<Attachment, Serializable> {

    @Query("select count(m) from Attachment a join a.messages m where a.id = ?1")
    Long countMessages(long id);

    Attachment findByMd5(String md5);

    @Query("select a from Attachment a where a.messages.size=0")
    Set<Attachment> findEmpty();
}
