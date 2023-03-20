package com.voda;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/main")
	public String main() {
		return "main";
	} 
	@RequestMapping("/register/view")
	public String registerView() {
		return "register";
	}
	
	@RequestMapping("/admin/content/edit") 
	public String adminContentEdit() {
		return "admin_content_edit";
	}
	@RequestMapping("/admin/content/list")
	public String adminContentList() {
		return "admin_content_list";
	}
	
}
