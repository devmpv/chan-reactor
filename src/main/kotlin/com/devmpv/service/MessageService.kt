package com.devmpv.service

import ac.simons.oembed.OembedService
import com.devmpv.config.Const.Companion.BOARD
import com.devmpv.config.Const.Companion.TEXT
import com.devmpv.config.Const.Companion.THREAD
import com.devmpv.config.Const.Companion.TITLE
import com.devmpv.config.WebSocketConfig.Companion.MESSAGE_PREFIX
import com.devmpv.exceptions.CRException
import com.devmpv.model.Attachment
import com.devmpv.model.Message
import com.devmpv.model.Thread
import com.devmpv.repositories.BoardRepository
import com.devmpv.repositories.MessageRepository
import com.devmpv.repositories.ThreadRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Document.OutputSettings
import org.jsoup.safety.Safelist
import org.nibor.autolink.Autolink
import org.nibor.autolink.LinkExtractor
import org.nibor.autolink.LinkType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.hateoas.server.EntityLinks
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*
import java.util.regex.Pattern
import javax.annotation.PostConstruct

/**
 * Service to manage basic message operations.
 *
 * @author devmpv
 */
@Service
class MessageService @Autowired
constructor(private val threadRepo: ThreadRepository,
            private val messageRepo: MessageRepository,
            private val boardRepo: BoardRepository,
            private val attachmentService: AttachmentService,
            private val template: SimpMessagingTemplate,
            private val entityLinks: EntityLinks,
            private val oembedService: OembedService) {

    private val pattern = Pattern.compile(REPLY_STRING)

    private val linkExtractor = LinkExtractor.builder().linkTypes(EnumSet.of(LinkType.URL)).build()

    private val textSettings = OutputSettings()

    @Value("\${chan.message.maxCount}")
    private val messageMaxCount: Int = 0

    @Value("\${chan.message.bumpLimit}")
    private val messageBumpLimit: Int = 0

    @Value("\${chan.thread.maxCount}")
    private val threadMaxCount: Int = 0

    private fun getPath(message: Message): String {
        return entityLinks.linkToItemResource(message.javaClass, message.id!!).toUri().path
    }

    private fun notify(message: Message) {
        val headers = HashMap<String, Any>()
        headers.put("thread", message.thread!!.id!!)
        template.convertAndSend("$MESSAGE_PREFIX/newMessage", getPath(message), headers)
    }

    @PostConstruct
    private fun postConstruct() {
        textSettings.prettyPrint(false)
    }

    private fun parseMentions(message: Message, thread: Thread): Message {
        var text = message.text
        val matcher = pattern.matcher(text)
        val replyIds = HashSet<Long>()
        while (matcher.find()) {
            replyIds.add(java.lang.Long.parseLong(matcher.group(1)))
        }
        val mentions = messageRepo.findByThreadAndIdIn(thread, replyIds)
        if (replyIds.contains(thread.id)) {
            mentions.plus(thread)
        }
        for (m in mentions) {
            text = text.replace(String.format(REPLY_QUOTE, m.id.toString()).toRegex(), REPLY_REPLACE)
        }
        message.text = text
        message.replyTo = mentions.toMutableSet()
        return message
    }

    private fun parseText(input: String): String {
        var result = Jsoup.clean(input, "", Safelist.basic(), textSettings)

        val links = linkExtractor.extractLinks(result)
        result = Autolink.renderLinks(result, links) { link, text, sb ->
            val url = text.subSequence(link.beginIndex, link.endIndex).toString()
            val response = oembedService.getOembedResponseFor(url)
            sb.append("<a href=\"").append(url).append("\">").append(url).append("</a>")
            response.ifPresent { oembedResponse -> sb.append("<div>").append(oembedResponse.html).append("</div>") }
        }
        return result
    }

    private fun saveAttachments(message: Message, files: Map<String, MultipartFile>): Message {
        val result = HashSet<Attachment>()
        val errors = HashSet<String>()
        files.forEach { name, file ->
            try {
                val attach = attachmentService.add(file)
                if (attach != null) {
                    result.add(attach)
                }
            } catch (e: IllegalStateException) {
                errors.add(name)
                throw CRException(e.message!!, e)
            } catch (e: IOException) {
                errors.add(name)
                throw CRException(e.message!!, e)
            }
        }
        if (!errors.isEmpty()) {
            throw CRException("Error saving files: " + errors.toString())
        }
        message.attachments = result
        return message
    }

    private fun saveMessage(params: Map<String, Array<String>>, files: Map<String, MultipartFile>, text: String): Message {
        val thread = threadRepo.findById(java.lang.Long.valueOf(params[THREAD]!![0])).get()
        var message = Message(params[TITLE]!![0], parseText(text))
        message.thread = thread
        saveAttachments(message, files)
        val count = messageRepo.countByThreadId(thread.id)!!
        if (count < messageBumpLimit) {
            thread.updatedAt = message.createdAt
            threadRepo.save(thread)
        }
        if (count >= messageMaxCount) {
            throw CRException("Thread limit exceeded!")
        }
        message = messageRepo.save(parseMentions(message, thread))
        return message
    }

    @Transactional
    fun saveNew(params: Map<String, Array<String>>, files: Map<String, MultipartFile>): Message {
        val result: Message
        if (params.containsKey(THREAD)) {
            result = saveMessage(params, files, params[TEXT]!![0])
            notify(result)
        } else {
            result = saveThread(params, files, params[TEXT]!![0])
        }
        return result
    }

    private fun saveThread(params: Map<String, Array<String>>, files: Map<String, MultipartFile>, text: String): Thread {
        val parsedText = parseText(text)
        val board = boardRepo.findById(params[BOARD]!![0]).get()
        val count = threadRepo.countByBoard(board)!!
        if (count >= threadMaxCount) {
            val threads = threadRepo.findByBoardOrderByUpdatedAtAsc(board)
            for (i in 0 until count - (threadMaxCount - 1)) {
                threadRepo.delete(threads.get(i.toInt()))
            }
        }
        val thread = saveAttachments(Thread(board, params[TITLE]!![0], parsedText), files) as Thread
        return threadRepo.save(thread)
    }

    companion object {

        private val REPLY_QUOTE = "&gt;&gt;(%s)"
        private val REPLY_STRING = String.format(REPLY_QUOTE, "[0-9]{1,8}")
        private val REPLY_REPLACE = "<a id='reply-link' key='$1'>$1</a>"
    }
}
