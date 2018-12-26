package jp.co.rakus.ec2018c.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.ec2018c.domain.LoginUser;
import jp.co.rakus.ec2018c.domain.User;

@Controller
@RequestMapping("/")
public class SendMailController {
	
	@Autowired
	private MailSender mailSender;
	
	@RequestMapping("/indexToMail")
	public String indexToMail(){
		return "test_send_mail";
	}
	
	@RequestMapping("/sendMail")
	public List<String> sendMail (@AuthenticationPrincipal LoginUser loginUser){
		User user = loginUser.getUser();
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(user.getEmail());
		msg.setTo("yk001214@gmail.com");
		msg.setSubject("注文確認メール");
		
	
		msg.setText( user.getName() +"さんより注文がありました");
		mailSender.send(msg);
		return Arrays.asList("OK");
	}

}
