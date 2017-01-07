package com.devmpv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.devmpv.service.MessageService;

@BasePathAwareController
public class OperationsController {

	@Autowired
	private MessageService messageService;

	@PostMapping("/res/submit")
	public ResponseEntity<?> handleNewMessage(MultipartHttpServletRequest request) {
		try {
			messageService.saveNew(request.getParameterMap(), request.getFileMap());
		} catch (Exception e) {
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
}
