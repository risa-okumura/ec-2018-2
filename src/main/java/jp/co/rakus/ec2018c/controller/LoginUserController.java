package jp.co.rakus.ec2018c.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jp.co.rakus.ec2018c.domain.LoginUser;
import jp.co.rakus.ec2018c.domain.Order;
import jp.co.rakus.ec2018c.domain.User;
import jp.co.rakus.ec2018c.service.ShoppingCartBadgeService;
import jp.co.rakus.ec2018c.service.ViewCartService;

@Controller
@RequestMapping("/")
// XXX:Userオブジェクトをセッションに入れる場合はこのように@SessionAttributesを使う方法もあります。
// こうするとmodel.addAttribute("user",user);でセッションに入れることができます。でも使ってる？？
@SessionAttributes( types = {User.class})
public class LoginUserController {

	@Autowired
	private ViewCartService service;
	
	@Autowired
	private ShoppingCartBadgeService shoppingCartBadgeService;

	@Autowired
	HttpSession session;
	
	@RequestMapping("/")
	public String index( Model model,
			@RequestParam(required = false) String error,
			@AuthenticationPrincipal LoginUser loginUser) {
		System.err.println("login error:" + error);
		if (error != null) {
			System.err.println("user: login failed");
			model.addAttribute("loginError","メールアドレスまたはパスワードが不正です");
		}
		
		Integer userId;
		
		if(loginUser == null) {
			userId = session.getId().hashCode();
		}else {
			User user = loginUser.getUser();
			userId = user.getId();
		}
		Integer status = 0;
		
		Order order = service.viewCart(userId, status);
		if(order != null) {
			Integer cartCount = shoppingCartBadgeService.countByOrderId(order.getId());
			model.addAttribute("cartCount", cartCount);
		}

		return "login";
	}
	
	
}
