package jp.co.rakus;

import org.springframework.batch.item.file.transform.LineAggregator;

import jp.co.rakus.ec2018c.domain.Order;

public class SampleAggregator implements LineAggregator<Order> {
	
	@Override
	public String aggregate(Order order) {
		StringBuilder sb = new StringBuilder();
		
	sb.append(order.getId());
	sb.append(",");
	sb.append(order.getUserId());
	sb.append(",");
	sb.append(order.getStatus());
	sb.append(",");
	sb.append(order.getTotalPrice());
	sb.append(",");
	sb.append(order.getOrderDate());
	sb.append(",");
	sb.append(order.getDestinationName());
	sb.append(",");
	sb.append(order.getDestinationEmail());
	sb.append(",");
	sb.append(order.getDestinationZipcode());
	sb.append(",");
	sb.append(order.getDestinationTel());
	sb.append(",");
	sb.append(order.getDeliveryTime());
	sb.append(",");
	sb.append(order.getUser().getId());
	sb.append(",");
	sb.append(order.getUser().getName());
	sb.append(",");
	sb.append(order.getUser().getEmail());
	sb.append(",");
	sb.append(order.getUser().getPassword());
	sb.append(",");
	sb.append(order.getUser().getZipcode());
	sb.append(",");
	sb.append(order.getUser().getAddress());
	sb.append(",");
	sb.append(order.getUser().getTelephone());
	
	return sb.toString();
		
	}
	

}
