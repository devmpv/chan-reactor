package com.devmpv.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.devmpv.model.Attachment;
import com.devmpv.repositories.AttachmentRepo;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class AttachmentService {
	private static final Logger LOG = LoggerFactory.getLogger(AttachmentService.class);

	private Path storagePath;
	private Path thumbPath;
	private AttachmentRepo repo;

	@Autowired
	public AttachmentService(@Value("${chan.file.path}") String filestorage, AttachmentRepo repo) throws Exception {
		this.repo = repo;
		this.storagePath = Paths.get(filestorage.replaceFirst("^~", System.getProperty("user.home")));
		this.thumbPath = storagePath.resolve("thumbs");
		checkPathExists(storagePath);
		checkPathExists(thumbPath);
	}

	public Attachment add(MultipartFile value) throws IOException {
		Attachment attach = null;
		String md5 = "";
		try {
			md5 = String.valueOf(DigestUtils.md5DigestAsHex(value.getInputStream()));
			attach = repo.findByMd5(md5);
			if (null != attach) {
				return attach;
			}
			String savedName = md5.concat(getExtension(value.getOriginalFilename()));
			Path savedPath = storagePath.resolve(savedName);
			if (!Files.exists(savedPath)) {
				Files.copy(value.getInputStream(), savedPath);
			}
			if (!Files.exists(thumbPath.resolve(savedName))) {
				Thumbnails.of(value.getInputStream()).size(150, 150).toFile(thumbPath.resolve(savedName).toString());
			}
			attach = new Attachment();
			attach.setMd5(md5);
			attach.setName(savedName);
			attach = repo.save(attach);
			return attach;
		} catch (IOException e) {
			LOG.error("Error saving attachment", e);
			throw new IOException("Error saving attachment", e);
		} finally {
			if (!md5.isEmpty() && null == attach) {
				Files.deleteIfExists(storagePath.resolve(md5));
			}
		}
	}

	private void checkPathExists(Path path) throws IOException {
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				LOG.error(String.format("Unable to create directory [%s]", path.toString()), e);
				throw e;
			}
		}
	}

	private String getExtension(String filename) {
		int dotIndex = filename.lastIndexOf('.');
		if (dotIndex > 0) {
			return filename.substring(dotIndex, filename.length());
		} else {
			return "";
		}
	}

	private File getFile(Attachment attachment) {
		return storagePath.resolve(attachment.getMd5()).toFile();
	}

	public Map<Attachment, File> getFileSet(Set<Attachment> attach) {
		Map<Attachment, File> result = new HashMap<>();
		attach.stream().filter(a -> Files.exists(storagePath.resolve(a.getMd5())))
				.forEach(a -> result.put(a, getFile(a)));
		return result;
	}

	public Resource loadAsResource(String filename) {
		Path file = storagePath.resolve(filename);
		Resource resource = null;
		try {
			resource = new UrlResource(file.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return resource;
	}

	public void store(MultipartFile file) {
		try {
			Files.copy(file.getInputStream(), this.storagePath.resolve(file.getOriginalFilename()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
