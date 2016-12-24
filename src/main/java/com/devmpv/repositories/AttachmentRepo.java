package com.devmpv.repositories;

import com.devmpv.model.Attachment;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

public interface AttachmentRepo extends CrudRepository<Attachment, Serializable> {
	Attachment findByMd5(String md5);
}
