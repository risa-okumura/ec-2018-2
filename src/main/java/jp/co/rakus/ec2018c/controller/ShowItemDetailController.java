package jp.co.rakus.ec2018c.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.ec2018c.domain.Item;
import jp.co.rakus.ec2018c.domain.LoginUser;
import jp.co.rakus.ec2018c.domain.Order;
import jp.co.rakus.ec2018c.domain.Topping;
import jp.co.rakus.ec2018c.domain.User;
import jp.co.rakus.ec2018c.form.AddCartForm;
import jp.co.rakus.ec2018c.service.ShoppingCartBadgeService;
import jp.co.rakus.ec2018c.service.ShowItemDetailService;
import jp.co.rakus.ec2018c.service.ViewCartService;

/**
 * 商品の詳細画面を表示するコントローラ.
 * 
 * @author momo.senda
 *
 */
@Controller
@RequestMapping("/ShowItemDetail")
public class ShowItemDetailController {
	@Autowired
	private ShowItemDetailService  showItemDetailService;
	
	@Autowired
	private ViewCartService service;
	
	@Autowired
	private ShoppingCartBadgeService shoppingCartBadgeService;
	
	@Autowired
	private HttpSession session;
	
	@ModelAttribute
	public AddCartForm setUpForm() {
		return new AddCartForm();
	}

	/**
	 * 商品の詳細情報を取得して、画面に表示する.
	 * 
	 * @param id 商品のid
	 * @param model モデル
	 * @return　商品の詳細情報
	 */
	@RequestMapping("/detail/{id}")
	public String index(@PathVariable("id") int id,Model model,@AuthenticationPrincipal LoginUser loginUser) {
		
		Item item=showItemDetailService.load(id);
		
		//不正なidが呼ばれた場合
		if(item == null) {
			return "redirect:/404";
		}

		List<Topping> toppingList=showItemDetailService.findAll();
		
		model.addAttribute("item",item);
		model.addAttribute("toppingList",toppingList);
		//ログイン認証からユーザー情報を取得し、ユーザーIDに代入.
		Integer userId;
		
		if(loginUser == null) {
			userId = session.getId().hashCode();
		}else {
			User user = loginUser.getUser();
			userId = user.getId();
		}
		
		//未購入の注文情報を指定.
		Integer status = 0;
		
		Order order = service.viewCart(userId, status);
		if(order != null) {
			Integer cartCount = shoppingCartBadgeService.countByOrderId(order.getId());
			model.addAttribute("cartCount", cartCount);
		}
		return "itemDetail";
	}
}
