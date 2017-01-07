package com.devmpv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping(value = { "/{board}" }, name = "boardController")
	public String board() {
		return "index";
	}

	@RequestMapping(value = { "/" }, name = "homeController")
	public String index() {
		return "index";
	}

	@RequestMapping(value = { "/{board}/thread/{id}" }, name = "threadController")
	public String thread() {
		return "index";
	}
}
