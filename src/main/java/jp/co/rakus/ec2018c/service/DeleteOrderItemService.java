package jp.co.rakus.ec2018c.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.rakus.ec2018c.repository.OrderItemRepository;

/**
 * 注文商品の情報を削除するサービス.
 * 
 * @author risa.okumura
 *
 */
@Service
public class DeleteOrderItemService {
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	/**
	 * 注文商品をショッピングカートから削除する.
	 * 
	 * @param orderItemId 注文商品のID
	 */
	public void deleteByOrderItemId(Integer orderItemId) {
		orderItemRepository.deleteById(orderItemId);
		
	}

}
