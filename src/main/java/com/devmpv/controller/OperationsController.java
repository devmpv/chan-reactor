package com.devmpv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.devmpv.service.MessageService;

/**
 * Controller for handling new message/thread actions.
 * 
 * @author devmpv
 *
 */
@BasePathAwareController
public class OperationsController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/res/submit")
    public ResponseEntity<Long> handleNewMessage(MultipartHttpServletRequest request) {
	Long id = messageService.saveNew(request.getParameterMap(), request.getFileMap()).getId();
	return ResponseEntity.ok(id);
    }
}
