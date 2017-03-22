package com.devmpv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Primary resource controller to handle common URIs requests
 * 
 * @author devmpv
 *
 */
@Controller
public class HomeController {

    private static final String INDEX = "index";

    @RequestMapping(value = { "/{board}" }, name = "boardController")
    public String board() {
	return INDEX;
    }

    @RequestMapping(value = { "/" }, name = "homeController")
    public String index() {
	return INDEX;
    }

    @RequestMapping(value = { "/{board}/thread/{id}" }, name = "threadController")
    public String thread() {
	return INDEX;
    }
}
