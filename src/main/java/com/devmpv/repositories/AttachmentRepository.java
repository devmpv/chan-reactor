package com.devmpv.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.devmpv.model.Attachment;

public interface AttachmentRepository extends CrudRepository<Attachment, Serializable> {
	@Query("select count(m) from Attachment a join a.messages m where a.id = ?1")
	long countMessages(long id);

	Attachment findByMd5(String md5);
}
