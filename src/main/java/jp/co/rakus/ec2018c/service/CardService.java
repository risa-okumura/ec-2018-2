package jp.co.rakus.ec2018c.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jp.co.rakus.ec2018c.domain.CardInfo;
import jp.co.rakus.ec2018c.domain.CardStatus;
import jp.co.rakus.ec2018c.domain.OrderNumber;

@Service
public class CardService {
	
	@Autowired
	@Qualifier("cardSearchRestTemplate")
	RestTemplate restTemplate;

	
//	@Autowired
//	RestOperations restOperations;
	
	private static final String URL = "http://172.16.0.13:8080/web-api-sample/credit-card/payment";
	private static final String CANCEL_URL = "http://172.16.0.13:8080/web-api-sample/credit-card/cancel";
	
	
	public ResponseEntity<CardStatus> cardInfoService(CardInfo cardInfo) {
		
//		CardStatus cardStatus = new CardStatus();
//		restOperations.getForObject(URL, CardStatus.class);
//		RequestEntity<CardStatus> requestEntity = RequestEntity
//				.post(URI.create("http://localhost:8080/cardInfo"))
//				.contentType(MediaType.APPLICATION_JSON)
//				.body(cardStatus);
//		
//		ResponseEntity<Void> responseEntity = restOperations.exchange(requestEntity, Void.class);
		
		return restTemplate.postForEntity(URL, cardInfo,CardStatus.class );
		
	}
	
	
	public CardStatus cardCancel(OrderNumber orderNumber) {
		
		return restTemplate.postForObject(CANCEL_URL, orderNumber, CardStatus.class);
		
		
	}
	
	

	
	

}
