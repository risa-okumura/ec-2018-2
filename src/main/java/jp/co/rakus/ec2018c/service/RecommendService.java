package jp.co.rakus.ec2018c.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.rakus.ec2018c.domain.Item;
import jp.co.rakus.ec2018c.repository.ItemRepository;

/**
 * ランダムで商品を3件取得するサービス
 * 
 * @author risa.okumura
 *
 */
@Service
public class RecommendService {
	
	@Autowired
	private ItemRepository itemRepository;

	/**
	 * 全件検索結果をシャッフルし、リストに3件取り出す.
	 * 
	 * @return　ランダムで商品が3つ入ったリスト
	 */
	public List<Item> recommend(){
		
	List<Item> itemList = itemRepository.findAll();
	Collections.shuffle(itemList);
	List<Item> itemRecommendList = new ArrayList<>();
	itemRecommendList.add(itemList.get(0));
	itemRecommendList.add(itemList.get(1));
	itemRecommendList.add(itemList.get(2));
	
	return itemRecommendList;
	}
	

}
