package com.devmpv.service;

import static com.devmpv.config.Const.Thumbs.HEIGHT;
import static com.devmpv.config.Const.Thumbs.WIDTH;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.devmpv.exceptions.CRException;
import com.devmpv.model.Attachment;
import com.devmpv.repositories.AttachmentRepository;

import net.coobird.thumbnailator.Thumbnails;

/**
 * Service to manipulate attachment entities and file storage.
 * 
 * @author devmpv
 *
 */
@Service
public class AttachmentService {
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentService.class);

    private Path storagePath;
    private Path thumbPath;
    private AttachmentRepository repo;

    @Autowired
    public AttachmentService(@Value("${chan.file.path}") String filestorage, AttachmentRepository repo)
	    throws IOException {
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
	    if (null == attach) {
		String savedName = md5.concat(getExtension(value.getOriginalFilename()));
		Path savedPath = storagePath.resolve(savedName);
		if (!savedPath.toFile().exists()) {
		    Files.copy(value.getInputStream(), savedPath);
		}
		if (!thumbPath.resolve(savedName).toFile().exists()) {
		    createThumbnail(value.getInputStream(), savedName);
		}
		attach = new Attachment();
		attach.setMd5(md5);
		attach.setName(savedName);
		attach = repo.save(attach);
	    }
	} catch (IOException e) {
	    LOG.error("Error saving attachment", e);
	    throw new IOException("Error saving attachment", e);
	} finally {
	    if (!md5.isEmpty() && null == attach) {
		Files.deleteIfExists(storagePath.resolve(md5));
	    }
	}
	return attach;
    }

    private void checkPathExists(Path path) throws IOException {
	if (!path.toFile().exists()) {
	    try {
		Files.createDirectories(path);
	    } catch (IOException e) {
		LOG.error(String.format("Unable to create directory [%s]", path.toString()), e);
		throw e;
	    }
	}
    }

    public void cleanup(Attachment attachment) {
	Path mainPath = storagePath.resolve(attachment.getName());
	Path thumbsPath = thumbPath.resolve(attachment.getName());
	try {
	    if (mainPath.toFile().exists())
		Files.delete(mainPath);
	    if (thumbsPath.toFile().exists())
		Files.delete(thumbsPath);
	} catch (IOException e) {
	    throw new CRException("Error while deleting attachment image!", e);
	}
    }

    private void createThumbnail(InputStream stream, String savedName) throws IOException {
	BufferedImage img = ImageIO.read(stream);
	if (img.getHeight() <= WIDTH && img.getWidth() <= HEIGHT) {
	    Thumbnails.of(img).size(img.getWidth(), img.getHeight()).toFile(thumbPath.resolve(savedName).toString());
	} else {
	    Thumbnails.of(img).size(WIDTH, HEIGHT).toFile(thumbPath.resolve(savedName).toString());
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
}
