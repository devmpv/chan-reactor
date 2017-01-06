package com.devmpv.repositories;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.devmpv.model.Attachment;

public interface AttachmentRepository extends CrudRepository<Attachment, Serializable> {
	Attachment findByMd5(String md5);
}
