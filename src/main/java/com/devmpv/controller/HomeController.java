package com.devmpv.controller;

import com.devmpv.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class HomeController {

	private MessageService messageService;

	@Autowired
	public HomeController(MessageService messageService) {
		this.messageService = messageService;
	}

	@RequestMapping(value = {"/{board}"}, name = "boardController")
	public String board() {
		return "index";
	}

	@PostMapping("/res/submit")
	public ResponseEntity<?> handleNewMessage(MultipartHttpServletRequest request) {
		try {
			messageService.saveNew(request.getParameterMap(), request.getFileMap());
		} catch (Exception e) {
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = {"/"}, name = "homeController")
	public String index() {
		return "index";
	}

	@RequestMapping(value = {"/{board}/thread/{id}"}, name = "threadController")
	public String thread() {
		return "index";
	}
}
