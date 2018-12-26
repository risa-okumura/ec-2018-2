package jp.co.rakus.ec2018c.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.ec2018c.domain.LoginUser;
import jp.co.rakus.ec2018c.domain.Order;
import jp.co.rakus.ec2018c.domain.User;
import jp.co.rakus.ec2018c.form.SearchItemForm;
import jp.co.rakus.ec2018c.repository.OrderItemRepository;
import jp.co.rakus.ec2018c.repository.OrderRepository;

/**
 * ログイン後処理を行う.
 * 
 * @author kento.uemura
 *
 */
@Controller
@RequestMapping("/loginafter")
public class LoginAfterController {
	@Autowired
	private ViewItemListController viewItemListController;
	@Autowired
	private OrderController orderController;
	@Autowired
	private HttpSession session;
	
	@ModelAttribute
	public SearchItemForm setUpForm() {
		return new SearchItemForm();
	}
	
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	/**
	 * ログイン後処理でsessionIdをuserIdに置き換える.
	 * 
	 * @param model モデル
	 * @param loginUser ログインユーザ情報
	 * @return 商品一覧画面
	 */
	@RequestMapping("/process")
	public String loginAfterProcess(Model model,@AuthenticationPrincipal LoginUser loginUser) {
		Integer sessionId = (Integer) session.getAttribute("sessionId");
		if(sessionId != null) {
			Integer status = orderController.UNORDERED_ID;
			Order sessionOrder = orderRepository.findByUserIdAndStatus(sessionId, status);

			User user = loginUser.getUser();
			Order loginUserOrder = orderRepository.findByUserIdAndStatus(user.getId(), status);
			
			if(loginUserOrder != null) {
				if(sessionOrder != null && loginUserOrder != null) {
					orderItemRepository.updateToOrderId(sessionOrder.getId(), loginUserOrder.getId());
					orderRepository.deleteById(sessionOrder.getId());
				}
			}else {
				if(sessionOrder != null) {
					sessionOrder.setUserId(user.getId());
					orderRepository.save(sessionOrder);
				}
			}
		}
		String pageNumber = null;
		return viewItemListController.list(pageNumber,model, loginUser);
	}
}
