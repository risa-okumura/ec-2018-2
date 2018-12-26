package jp.co.rakus.ec2018c.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.rakus.ec2018c.domain.Item;
import jp.co.rakus.ec2018c.domain.Topping;
import jp.co.rakus.ec2018c.repository.ItemRepository;
import jp.co.rakus.ec2018c.repository.ToppingRepository;

/**
 * itemの詳細表示に関するサービスクラス.
 * 
 * @author momo.senda
 *
 */
@Service
public class ShowItemDetailService {
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private ToppingRepository toppingRepository;

	
	public Item load(Integer id) {
		return itemRepository.load(id);
	}

	public List<Topping> findAll() {
		return toppingRepository.findAll();
	}

}
