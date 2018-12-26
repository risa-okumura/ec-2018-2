package jp.co.rakus.ec2018c.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.rakus.ec2018c.domain.Item;
import jp.co.rakus.ec2018c.domain.LoginUser;
import jp.co.rakus.ec2018c.domain.Order;
import jp.co.rakus.ec2018c.domain.User;
import jp.co.rakus.ec2018c.form.SearchItemForm;
import jp.co.rakus.ec2018c.repository.ItemRepository;
import jp.co.rakus.ec2018c.service.ShoppingCartBadgeService;
import jp.co.rakus.ec2018c.service.ViewCartService;
import jp.co.rakus.ec2018c.service.ViewItemListService;

/**
 * 	商品の検索を行うコントローラー.
 * 
 * @author momo.senda
 *
 */
@Controller
@Transactional
@RequestMapping("/SearchItem")
public class SearchItemController {
	
	@Autowired
	private ViewItemListService viewItemListService;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ViewCartService service;
	
	@Autowired
	private ShoppingCartBadgeService shoppingCartBadgeService;
	
	@Autowired
	private HttpSession session;

	@ModelAttribute
	public SearchItemForm setUpForm() {
		return new SearchItemForm();
	}
	
	@RequestMapping("/search")
	public String searchItem(Model model,@RequestParam(name = "pageNum", required = false) String pageNumber,@Validated SearchItemForm searchItemForm, BindingResult result,@AuthenticationPrincipal LoginUser loginUser) {
		
		
//		List<Item> itemList = new ArrayList<>();
//		itemList =viewItemListService.findByName(searchItemForm.getName());
		String name = searchItemForm.getName();
		
		List<Item> itemList = itemRepository.findByNameOffset(name,0);
		List<Integer> pageNumList = new ArrayList<>();
		if(itemList.size() == 0) {
			result.rejectValue("name", null, "該当する商品がありません");
			itemList = itemRepository.findItemByOffset(0);
			pageNumList = pageNumList();
			model.addAttribute("pageNumList", pageNumList);
			model.addAttribute("name", name);
			model.addAttribute("itemList", itemList);
			
			return "itemList";
		}else {
			pageNumList = searchPageNumList(name);
		}
		

		//ログイン認証からユーザー情報を取得し、ユーザーIDに代入.
		Integer userId;
		
		if(loginUser == null) {
			userId = session.getId().hashCode();
		}else {
			User user = loginUser.getUser();
			userId = user.getId();
		}
		
		pageNumList = searchPageNumList(name);
		model.addAttribute("pageNumList", pageNumList);
		model.addAttribute("name", name);
		
		// ページ番号1をクリックしたときに表示.
		if (pageNumber == null || pageNumber.equals("1")) {
			itemList = itemRepository.findByNameOffset(name,0);
			model.addAttribute("itemList", itemList);
			String firstPageNumber = "1";
			model.addAttribute("nowPageNum", firstPageNumber);
			return "itemList";

		}
		//クリックされたページ番号をもとに表示.
		Integer offset = Integer.parseInt(pageNumber);
		for(int i = 1 ; i <= Integer.parseInt(pageNumber)-1 ; i++) {
			offset = offset + 9;
		}
		System.out.println(offset);
		itemList = itemRepository.findByNameOffset(name,offset);
		model.addAttribute("nowPageNum", pageNumber);
		model.addAttribute("itemList", itemList);
		
		if(Integer.parseInt(pageNumber) > pageNumList.size()  ) {
			
			return "redirect:/404";
		}
		
		//未購入の注文情報を指定.
		Integer status = 0;
		
		Order order = service.viewCart(userId, status);
		if(order != null) {
			Integer cartCount = shoppingCartBadgeService.countByOrderId(order.getId());
			model.addAttribute("cartCount", cartCount);
		}
		return "itemList";
		
	}
	
//	@RequestMapping("/searchOtherlist")
//	public String otherlist(@RequestParam(name = "pageNum", required = false) String pageNumber,@RequestParam(name = "name", required = false) String name,Model model, @AuthenticationPrincipal LoginUser loginUser) {
//
//		Integer userId;
//		if (loginUser == null) {
//			userId = session.getId().hashCode();
//		} else {
//			User user = loginUser.getUser();
//			userId = user.getId();
//		}
//		// 未購入の注文情報を指定.
//		Integer status = 0;
//		Order order = service.viewCart(userId, status);
//		if (order != null) {
//			Integer cartCount = shoppingCartBadgeService.countByOrderId(order.getId());
//			model.addAttribute("cartCount", cartCount);
//		}
//
//		
//		List<Integer> pageNumList = searchPageNumList(name);
//		if(Integer.parseInt(pageNumber) > pageNumList.size()  ) {
//			
//			return "redirect:/404";
//		}
//		model.addAttribute("pageNumList", pageNumList);
//		model.addAttribute("name", name);
//		
//		// ページ番号1をクリックしたときに表示.
//		if (pageNumber == null || pageNumber.equals("1")) {
//			List<Item> itemList = itemRepository.findByNameOffset(name,0);
//			model.addAttribute("itemList", itemList);
//			return "itemList";
//
//		}
//		//クリックされたページ番号をもとに表示.
//		Integer offset = Integer.parseInt(pageNumber);
//		for(int i = 1 ; i <= Integer.parseInt(pageNumber)-1 ; i++) {
//			offset = offset + 9;
//		}
//		System.out.println(offset);
//		List<Item> itemList = itemRepository.findByNameOffset(name,offset);
//		model.addAttribute("itemList", itemList);
//		
//		
//		return "itemList";
//
//	}
	
	public List<Integer> searchPageNumList(String name) {
		Integer countAllItem = itemRepository.countFindByName(name);
		System.out.println(countAllItem);
		Integer maxPage = (countAllItem/9)+1;
		List<Integer> pageNumList = new ArrayList<>();
		for(int i = 1; i <= maxPage ; i++) {
			pageNumList.add(i);
		}
		
		return pageNumList;

	}
	
	public List<Integer> pageNumList() {
		Integer countAllItem = itemRepository.countAllItem();
		System.out.println(countAllItem);
		Integer maxPage = (countAllItem/9)+1;
		List<Integer> pageNumList = new ArrayList<>();
		for(int i = 1; i <= maxPage ; i++) {
			pageNumList.add(i);
		}
		
		return pageNumList;

	}

 

	
}
