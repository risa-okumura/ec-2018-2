package jp.co.rakus.ec2018c.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.ec2018c.domain.CardInfo;
import jp.co.rakus.ec2018c.domain.CardStatus;
import jp.co.rakus.ec2018c.domain.Item;
import jp.co.rakus.ec2018c.domain.LoginUser;
import jp.co.rakus.ec2018c.domain.Order;
import jp.co.rakus.ec2018c.domain.OrderItem;
import jp.co.rakus.ec2018c.domain.OrderNumber;
import jp.co.rakus.ec2018c.domain.User;
import jp.co.rakus.ec2018c.form.OrderDestinationForm;
import jp.co.rakus.ec2018c.service.CardService;
import jp.co.rakus.ec2018c.service.OrderService;
import jp.co.rakus.ec2018c.service.ShoppingCartBadgeService;

/**
 * 注文処理を行うコントローラ.
 * 
 * @author kento.uemura
 *
 */
@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private ShoppingCartBadgeService shoppingCartBadgeService;
	@Autowired
	private CardService cardService;

	@Autowired
	private MailSender mailSender;

	// 未注文のstatus
	public Integer UNORDERED_ID = 0;

	@ModelAttribute
	public OrderDestinationForm setUpForm() {
		return new OrderDestinationForm();
	}

	/**
	 * 注文情報を表示する. 注文情報をuseridとstatus(0:未注文)で調べてmodelに格納する
	 * 
	 * @param model
	 *            モデル
	 * @return 注文表示画面
	 */
	@RequestMapping("/orderconfirm")
	public String index(Model model, @AuthenticationPrincipal LoginUser loginUser, boolean error) {
		Integer status = UNORDERED_ID;
		// ログイン中のユーザを取得する
		User user = loginUser.getUser();
		Integer userId = user.getId();

		Order order = orderService.findByUserIdAndStatus(userId, status);
		Integer cartCount = shoppingCartBadgeService.countByOrderId(order.getId());
		model.addAttribute("cartCount", cartCount);
		model.addAttribute("order", order);
		if (!error) {
			model.addAttribute("user", user);
		}
		return "orderconfirm";
	}

	/**
	 * 注文する.
	 * 
	 * @param form 配達先情報が入ったフォーム
	 * @param result リザルト
	 * @param loginUser ログインユーザ情報
	 * @return 注文完了画面
	 * 入力フォームのエラーが起きたときは確認画面に戻す
	 */
	@RequestMapping("/order")
	public String order(@Validated OrderDestinationForm form,
									BindingResult result,Model model,
									@AuthenticationPrincipal LoginUser loginUser) {
		Timestamp timestamp = null;
		if(result.getFieldErrorCount("deliveryDate") == 0) {
			timestamp = orderService.stringToTimestamp(form.getDeliveryDate()+","+form.getDeliveryTime());
			LocalDateTime localDateTime = LocalDateTime.now();
			Timestamp nowTimestampPlusOneHour = Timestamp.valueOf(localDateTime.plusHours(1));
			if(!nowTimestampPlusOneHour.before(timestamp)) {
				result.rejectValue("deliveryDate", null, "配達日時は現時刻の1時間以降を指定してください");
			}
		}
		
		if(result.hasErrors()) {
			return index(model,loginUser,true);
		}
		
		Integer status = UNORDERED_ID;
		//ログイン中のユーザを取得する
		User user = loginUser.getUser();
		Integer userId = user.getId();
		
		Order order = orderService.findByUserIdAndStatus(userId, status);

		//formの内容をorderに詰める
		order.setStatus(Integer.valueOf(form.getPaymentMethod()));
		order.setTotalPrice(order.getCalcTotalPrice());
		order.setOrderDate(new Date());
		order.setDestinationName(form.getDestinationName());
		order.setDestinationEmail(form.getDestinationEmail());
		order.setDestinationZipcode(form.getDestinationZipcode());
		order.setDestinationAddress(form.getDestinationAddress());
		order.setDestinationTel(form.getDestinationTel());
		order.setDeliveryTime(timestamp);
		order.setPaymentMethod(Integer.valueOf(form.getPaymentMethod()));
		
		
		//クレジットカード情報をwebAPIで決済する。
		if(order.getPaymentMethod()==2) {
			
		CardInfo cardInfo = new CardInfo();
		BeanUtils.copyProperties(form, cardInfo);
		cardInfo.setUser_id(userId);
		cardInfo.setOrder_number(order.getId());
		cardInfo.setAmount(order.getCalcTotalPrice());
		
		ResponseEntity<CardStatus> responseEntity = cardService.cardInfoService(cardInfo);
		CardStatus cardStatus = responseEntity.getBody();
		System.out.println(cardStatus.getStatus() + ":" + cardStatus.getMessage());
		
			if(cardStatus.getStatus().equals("error")) {
				result.rejectValue("card_number", null, "クレジットカード情報が不正です");
				return index(model,loginUser,true); 
			}else {
				try {
					orderService.update(order);
				}catch(Exception e) {
					System.err.println("不正なSQL文です");
					OrderNumber orderNumber = new OrderNumber();
					orderNumber.setOrder_number(order.getId());
					cardStatus = cardService.cardCancel(orderNumber);
					System.err.println("決済処理をキャンセルしました");
					System.out.println(cardStatus.getStatus());
					return index(model,loginUser,true); 
				}
			}
			
		}else{
		orderService.update(order);
		}
		
		//注文確認メールを送る
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo("risa.okumura@rakus.co.jp");
		msg.setSubject("【ラクラクピザ】ご注文ありがとうございます【内容確認】");
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(user.getName()).append("様");
		stringBuilder.append("\n");
		stringBuilder.append("この度はラクラクピザをご利用いただきありがとうございます！");
		stringBuilder.append("\n");
		stringBuilder.append("下記内容にてご注文を承りましたので、ご確認ください。");
		stringBuilder.append("\n");
		stringBuilder.append("【ご注文内容】");
		stringBuilder.append("\n");
		for(OrderItem orderItem : order.getOrderItemList()) {
			stringBuilder.append(orderItem.getItem().getName());
			stringBuilder.append("\n");
		}
		stringBuilder.append(order.getCalcTotalPrice());
		stringBuilder.append("円");
		stringBuilder.append("\n");
		
		msg.setText(stringBuilder.toString());
		mailSender.send(msg);
		
		
		//注文完了画面にリダイレクトで遷移
		return "redirect:/order/finish";
	}

	/**
	 * 注文完了画面を表示する.
	 * 
	 * @return 注文完了画面
	 */
	@RequestMapping("/finish")
	public String orderFinished() {
		
		
		
		return "orderfinished";
	}

}
