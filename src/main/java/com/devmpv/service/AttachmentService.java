package com.devmpv.service;

import com.devmpv.model.Attachment;
import com.devmpv.model.AttachmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AttachmentService {
	private static final Logger LOG = LoggerFactory.getLogger(AttachmentService.class);

	private Path storagePath;
	private AttachmentRepository repo;

	@Autowired
	public AttachmentService(@Value("${chan.file.path}") String filestorage, AttachmentRepository repo)
			throws Exception {
		this.repo = repo;
		this.storagePath = Paths.get(filestorage.replaceFirst("^~", System.getProperty("user.home")));
		if (!Files.exists(storagePath)) {
			try {
				Files.createDirectories(storagePath);
			} catch (IOException e) {
				LOG.error("Unable to create attachment directory.", e);
				throw e;
			}
		}
	}

	public Attachment add(File value) throws Exception {
		Attachment attach = null;
		String md5 = "";
		try {
			md5 = String.valueOf(DigestUtils.md5DigestAsHex(new FileInputStream(value)));
			attach = repo.findByMd5(md5);
			if (null != attach) {
				throw new Exception("File alredy present on the board");
			}
			Files.copy(value.toPath(), storagePath.resolve(md5));
			attach = new Attachment();
			attach.setMd5(md5);
			attach = repo.save(attach);
			return attach;
		} catch (IOException e) {
			LOG.error("Error saving attachment", e);
			throw new Exception("Error saving attachment");
		} finally {
			if (!md5.isEmpty() && null == attach) {
				Files.deleteIfExists(storagePath.resolve(md5));
			}
		}
	}

	private File getFile(Attachment attachment) {
		return storagePath.resolve(attachment.getMd5()).toFile();
	}

	public Map<Attachment, File> getFileSet(Set<Attachment> attach) {
		Map<Attachment, File> result = new HashMap<>();
		attach.stream()
				.filter(a -> Files.exists(storagePath.resolve(a.getMd5())))
				.forEach(a -> result.put(a, getFile(a)));
		return result;
	}
}
