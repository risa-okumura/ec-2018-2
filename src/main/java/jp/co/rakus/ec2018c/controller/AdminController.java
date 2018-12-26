package jp.co.rakus.ec2018c.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@RequestMapping("/index")
	public String indexForAdmin() {
		return "admin_index";
	}
	
	@RequestMapping("/top")
	public String topForAdmin() {
		return "admin_index";
	}

}
