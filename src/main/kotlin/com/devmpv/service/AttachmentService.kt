package com.devmpv.service

import com.devmpv.config.Const.Thumbs.HEIGHT
import com.devmpv.config.Const.Thumbs.WIDTH
import com.devmpv.exceptions.CRException
import com.devmpv.model.Attachment
import com.devmpv.repositories.AttachmentRepository
import net.coobird.thumbnailator.Thumbnails
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO

/**
 * Service to manipulate attachment entities and file storage.
 *
 * @author devmpv
 */
@Service
class AttachmentService @Autowired
@Throws(IOException::class)
constructor(@Value("\${chan.file.path}") filestorage: String, private val repo: AttachmentRepository) {

    private val storagePath: Path
    private val thumbPath: Path

    init {
        this.storagePath = Paths.get(filestorage.replaceFirst("^~".toRegex(), System.getProperty("user.home")))
        this.thumbPath = storagePath.resolve("thumbs")
        checkPathExists(storagePath)
        checkPathExists(thumbPath)
    }

    @Throws(IOException::class)
    internal fun add(value: MultipartFile): Attachment? {
        var attach: Attachment? = null
        var md5 = ""
        try {
            md5 = DigestUtils.md5DigestAsHex(value.inputStream)
            val optional = repo.findByMd5(md5)
            if (!optional.isPresent) {
                val savedName = md5 + getExtension(value.originalFilename!!)
                val savedPath = storagePath.resolve(savedName)
                if (!savedPath.toFile().exists()) {
                    Files.copy(value.inputStream, savedPath)
                }
                if (!thumbPath.resolve(savedName).toFile().exists()) {
                    createThumbnail(value.inputStream, savedName)
                }
                attach = Attachment()
                attach.md5 = md5
                attach.name = savedName
                attach = repo.save(attach)
            } else {
                attach = optional.get()
            }
        } catch (e: IOException) {
            LOG.error("Error saving attachment", e)
            throw IOException("Error saving attachment", e)
        } finally {
            if (!md5.isEmpty() && attach == null) {
                Files.deleteIfExists(storagePath.resolve(md5))
            }
        }
        return attach!!
    }

    @Throws(IOException::class)
    private fun checkPathExists(path: Path) {
        if (!path.toFile().exists()) {
            try {
                Files.createDirectories(path)
            } catch (e: IOException) {
                LOG.error(String.format("Unable to create directory [%s]", path.toString()), e)
                throw e
            }

        }
    }

    internal fun cleanup(attachment: Attachment) {
        val mainPath = storagePath.resolve(attachment.name)
        val thumbsPath = thumbPath.resolve(attachment.name)
        try {
            if (mainPath.toFile().exists())
                Files.delete(mainPath)
            if (thumbsPath.toFile().exists())
                Files.delete(thumbsPath)
        } catch (e: IOException) {
            throw CRException("Error while deleting attachment image!", e)
        }

    }

    @Throws(IOException::class)
    private fun createThumbnail(stream: InputStream, savedName: String) {
        val img = ImageIO.read(stream)
        if (img.height <= WIDTH && img.width <= HEIGHT) {
            Thumbnails.of(img).size(img.width, img.height).toFile(thumbPath.resolve(savedName).toString())
        } else {
            Thumbnails.of(img).size(WIDTH, HEIGHT).toFile(thumbPath.resolve(savedName).toString())
        }
    }

    private fun getExtension(filename: String): String {
        val dotIndex = filename.lastIndexOf('.')
        return if (dotIndex > 0) {
            filename.substring(dotIndex, filename.length)
        } else {
            ""
        }
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(AttachmentService::class.java)
    }
}
