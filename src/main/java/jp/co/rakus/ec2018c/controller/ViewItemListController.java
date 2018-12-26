package jp.co.rakus.ec2018c.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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
 * itemの一覧表示を行うコントローラー
 * 
 * @author momo.senda
 *
 */
@Controller
@RequestMapping("/viewItemList")
@Transactional
public class ViewItemListController {

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

	/**
	 * itemの全情報を取得し、画面に表示する.
	 * 
	 * @param model
	 *            モデル
	 * @return item情報表示画面
	 */
//	@RequestMapping("/list")
//	public String list(Model model, @AuthenticationPrincipal LoginUser loginUser) {
//		List<Item> itemList = itemRepository.findItemByOffset(0);
//		model.addAttribute("itemList", itemList);
//		Integer userId;
//
//		if (loginUser == null) {
//			userId = session.getId().hashCode();
//		} else {
//			User user = loginUser.getUser();
//			userId = user.getId();
//		}
//
//		// 未購入の注文情報を指定.
//		Integer status = 0;
//
//		Order order = service.viewCart(userId, status);
//		if (order != null) {
//			Integer cartCount = shoppingCartBadgeService.countByOrderId(order.getId());
//			model.addAttribute("cartCount", cartCount);
//		}
//		
//		//表示されるページ数を指定.
//		List<Integer> pageNumList = pageNumList();
//		model.addAttribute("pageNumList", pageNumList);
//		
//		Integer viewPageNum = 1;
//		model.addAttribute("viewPageNum", viewPageNum);
//		
//		
//		return "itemList";
//	}

	@RequestMapping("/list")
	public String list(@RequestParam(name = "pageNum", required = false) String pageNumber,Model model, @AuthenticationPrincipal LoginUser loginUser) {

		Integer userId;

		if (loginUser == null) {
			userId = session.getId().hashCode();
		} else {
			User user = loginUser.getUser();
			userId = user.getId();
		}

		// 未購入の注文情報を指定.ページャー bootstrap 真ん中
		Integer status = 0;
		Order order = service.viewCart(userId, status);
		if (order != null) {
			Integer cartCount = shoppingCartBadgeService.countByOrderId(order.getId());
			model.addAttribute("cartCount", cartCount);
		}
		
		//表示されるページ数を指定.
		List<Integer> pageNumList = pageNumList();
		model.addAttribute("pageNumList", pageNumList);
		
		// ページ番号1をクリックしたときに表示.
		if (pageNumber == null || pageNumber.equals("1")) {
			List<Item> itemList = itemRepository.findItemByOffset(0);
			model.addAttribute("itemList", itemList);
			String firstPageNumber = "1";
			model.addAttribute("nowPageNum", firstPageNumber);
			return "itemList";

		}
		
		//不正なページナンバーがURLに渡されたら404エラーページに.
		if(Integer.parseInt(pageNumber) > pageNumList.size() ) {
			return "redirect:/404";
		}
		
		//クリックされたページ番号をもとに表示.
		Integer offset = Integer.parseInt(pageNumber);
		for(int i = 1 ; i <= Integer.parseInt(pageNumber)-1 ; i++) {
			offset = offset + 9;
		}
		List<Item> itemList = itemRepository.findItemByOffset(offset);
		model.addAttribute("itemList", itemList);
		
		model.addAttribute("nowPageNum", pageNumber);
		
		return "itemList";

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
