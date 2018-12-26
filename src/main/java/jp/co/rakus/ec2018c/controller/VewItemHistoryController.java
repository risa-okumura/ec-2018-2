package jp.co.rakus.ec2018c.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.rakus.ec2018c.domain.Item;
import jp.co.rakus.ec2018c.service.ShowItemDetailService;

@Controller
@RequestMapping("/")
public class VewItemHistoryController {
	
	@Autowired
	private ShowItemDetailService service;
	
	@ResponseBody
	@RequestMapping("/itemHistory")
	public Item itemHistory(Integer id){
		
		System.out.println(id);
		Item item = service.load(id);
		
		return item;
	}

}
