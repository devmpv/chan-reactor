package com.devmpv.controller

import com.devmpv.service.MessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.multipart.MultipartHttpServletRequest

/**
 * Controller for handling new message/thread actions.
 *
 * @author devmpv
 */
@Controller
class OperationsController @Autowired
constructor(private val messageService: MessageService) {

    @PostMapping("/res/submit")
    fun handleNewMessage(request: MultipartHttpServletRequest): ResponseEntity<Long> {
        val id = messageService.saveNew(request.parameterMap, request.fileMap).id
        return ResponseEntity.ok(id!!)
    }
}
