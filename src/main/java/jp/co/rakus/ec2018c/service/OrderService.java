package jp.co.rakus.ec2018c.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.rakus.ec2018c.domain.Order;
import jp.co.rakus.ec2018c.repository.OrderRepository;

/**
 * 注文情報を表示するためのサービス.
 * 
 * @author kento.uemura
 *
 */
@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd,h";

	/**
	 * UserId,Statusを指定して注文情報を取得する.
	 * 
	 * @param userId
	 *            ログインユーザのID
	 * @param status
	 *            注文の状態 (0:未注文,1:未入金,2:入金済み,3:発送済み,9:キャンセル)
	 * @return 取得した注文情報
	 */
	public Order findByUserIdAndStatus(Integer userId, Integer status) {
		return orderRepository.findByUserIdAndStatus(userId, status);
	}

	/**
	 * Orderの更新を行う.
	 * 
	 * @param order
	 *            更新情報が入れられたOrderドメイン
	 */
	public void update(Order order) {
		orderRepository.save(order);
	}

	/**
	 * 入力される日付をTimestampに変換する.
	 * 
	 * @param day
	 *            変換するString型の日時
	 * @return 変換された日時のTimestamp
	 */
	public Timestamp stringToTimestamp(String day) {
		try {
			return new Timestamp(new SimpleDateFormat(TIMESTAMP_FORMAT).parse(day).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateStatus(Integer status, long order_number) {
		orderRepository.updateStatus(status, order_number);
	}

	public List<Order> findAll() {
		return orderRepository.findALL();
	}

	public void findAllCsv() {
		orderRepository.findAllCsv();
	}

	public List<Long> findOrderIdByUserId(long userId) {
		return orderRepository.findOrderIdByUserId(userId);
	}
	
	public Integer countByOrderId(long orderId) {
		return orderRepository.countByOrderId(orderId);
	}

	public List<Order> findByUserId(long userId, boolean isJoin) {
		return orderRepository.findByUserId(userId, isJoin);
	}

}
