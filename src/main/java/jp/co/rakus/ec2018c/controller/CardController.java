package jp.co.rakus.ec2018c.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.rakus.ec2018c.domain.CardInfo;
import jp.co.rakus.ec2018c.domain.CardStatus;
import jp.co.rakus.ec2018c.form.CardInfoForm;
import jp.co.rakus.ec2018c.service.CardService;

@Controller
@RequestMapping("/")
public class CardController {
	
	@Autowired
	private CardService cardService;
	
	@ModelAttribute
	public CardInfoForm setUpForm() {
		return new CardInfoForm();
	}
	
	@RequestMapping("/cardInfo")
	public String toCardInfo() {
		return "cardInfo";
	}
	
	@RequestMapping(value = "/cardConfirm", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	public String cardConfirm(Model model,CardInfoForm cardInfoForm) {
		
//		String userId = "1";
//		String orderNumber = "1";
//		String amount = "10000";
//		cardInfo.setUserId(userId);
//		cardInfo.setOrderNumber(orderNumber);
//		cardInfo.setAmount(amount);
		
		CardInfo cardInfo = new CardInfo();
		
		BeanUtils.copyProperties(cardInfoForm, cardInfo);
		
		System.out.println(cardInfo.getCard_name());
	
		ResponseEntity<CardStatus> responseEntity = cardService.cardInfoService(cardInfo);
		CardStatus cardStatus = responseEntity.getBody();
		
		System.out.println(cardStatus.getStatus());
		System.out.println(cardStatus.getMessage());
		System.out.println(cardStatus.getError_code());
		
		model.addAttribute("cardStatus", cardStatus);
		
	return "cardConfirm";	
		
	}


}
