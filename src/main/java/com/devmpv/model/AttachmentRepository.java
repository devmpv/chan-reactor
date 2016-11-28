package com.devmpv.model;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

public interface AttachmentRepository extends CrudRepository<Attachment, Serializable> {
	Attachment findByMd5(String md5);
}
