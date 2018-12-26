package jp.co.rakus.ec2018c.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.rakus.ec2018c.repository.OrderItemRepository;

@Service
public class ChangeQuantityService {
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	public void updateQuantity(long orderId2,Integer orderItemId2,Integer quantity) {
		orderItemRepository.updateQuantity(orderId2, orderItemId2, quantity);
	}
	

}
