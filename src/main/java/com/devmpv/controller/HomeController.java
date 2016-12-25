package com.devmpv.controller;

import com.devmpv.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;

@Controller
public class HomeController {

	private AttachmentService attachmentService;

	@Autowired
	public HomeController(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	@RequestMapping(value = {"/"}, name = "homeController")
	public String index() {
		return "index";
	}

	@RequestMapping(value = {"/{board}"}, name = "boardController")
	public String board() {
		return "index";
	}

	@RequestMapping(value = {"/{board}/thread/{id}"}, name = "threadController")
	public String thread() {
		return "index";
	}

	@GetMapping("/res/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = attachmentService.loadAsResource(filename);
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@PostMapping("/files/upload")
	public ResponseEntity<?> handleFileUpload(MultipartHttpServletRequest request) {
		Map<String, MultipartFile> fileMap = request.getFileMap();
		//attachmentService.store(fileMap.get());
		return ResponseEntity.ok().build();
	}

	@ExceptionHandler
	public ResponseEntity handleStorageFileNotFound(Exception exc) {
		return ResponseEntity.notFound().build();
	}
}
