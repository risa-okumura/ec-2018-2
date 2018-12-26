package jp.co.rakus.ec2018c.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.rakus.ec2018c.domain.Item;
import jp.co.rakus.ec2018c.repository.ItemRepository;

/**
 * itemの一覧表示に関するサービスクラス.
 * 
 * @author momo.senda
 *
 */
@Service
public class ViewItemListService {
	@Autowired
	private ItemRepository itemRepository;

	/**
	 * 商品の全ての情報を取得するサービスクラス
	 * 
	 * @return　取得した商品の情報
	 */
	public List<Item> findAll() {
		return itemRepository.findAll();
	}
	
	/**
	 * 名前から商品を検索する
	 * 
	 * @param name　商品の名前
	 * 
	 * @return　検索された商品情報
	 */
	public List<Item> findByName(String name){
		return itemRepository.findByName(name);
	}

}
