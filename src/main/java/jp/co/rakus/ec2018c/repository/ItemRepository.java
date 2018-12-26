package jp.co.rakus.ec2018c.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

import jp.co.rakus.ec2018c.domain.Item;

/**
 * Itemテーブル操作用のリポジトリ.
 * 
 * @author momo.senda
 *
 */
@Repository
public class ItemRepository {
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


	/**
	 * ResultSetオブジェクトからItemオブジェクトに変換するためのクラス実装&インスタンス化.
	 */
	private static final RowMapper<Item> ITEM_ROW_MAPPER = (rs, i) -> {
	Integer id = rs.getInt("id");
	String name = rs.getString("name");
	String description = rs.getString("description");
	Integer priceM = rs.getInt("price_m");
	Integer priceL = rs.getInt("price_l");
	String imagePath = rs.getString("image_path");
	Boolean deleted = rs.getBoolean("deleted");
	return new Item(id, name, description, priceM, priceL, imagePath, deleted);

};

	/**
	 * 商品の全情報を取得する
	 * 
	 * @return 取得したピザの情報
	 */
	public List<Item> findAll() {
		List<Item> items = namedParameterJdbcTemplate.query(
				"SELECT id,name,description,price_m,price_l,image_path,deleted FROM items ORDER BY price_m DESC",
				ITEM_ROW_MAPPER);
		return items;
	}

	/**
	 * 名前から商品を(曖昧)検索する.
	 * 
	 * @param name
	 *            商品の名前
	 * @return 検索された商品の情報
	 */
	public List<Item> findByName(String name) {
		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted FROM items WHERE name LIKE :name ORDER BY name DESC ";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");

		List<Item> itemList = namedParameterJdbcTemplate.query(sql, param, ITEM_ROW_MAPPER);
		return itemList;

	}
	
	public List<Item> findByNameOffset(String name,int offset) {
		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted FROM items WHERE name LIKE :name ORDER BY name DESC LIMIT 9 OFFSET :offset;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("offset", offset);

		List<Item> itemList = namedParameterJdbcTemplate.query(sql, param, ITEM_ROW_MAPPER);
		return itemList;

	}
	
	public Integer countFindByName (String name) {
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		String sql = "SELECT COUNT(*) FROM items WHERE name LIKE :name;";
		Integer countItem = namedParameterJdbcTemplate.queryForObject(sql, param, Integer.class);
		return countItem;
	}
	
	

	/**
	 * idから商品を1件検索する.
	 * 
	 * @param id
	 *            商品id
	 * @return
	 */
	public Item load(Integer id) {
		String sql="SELECT id,name,description,price_m,price_l,image_path,deleted FROM items WHERE id= :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Item> itemList=namedParameterJdbcTemplate.query(sql, param, ITEM_ROW_MAPPER);
		
		if(itemList.size()==0) {
			return null;
		}
		
		return itemList.get(0);
		
	}
	
	
	/**
	 * 最初の10件を検索する.
	 * 
	 * @return
	 */
	public List<Item> findItemByOffset(int offset){
		SqlParameterSource param = new MapSqlParameterSource().addValue("offset", offset);
		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted FROM items ORDER BY price_m DESC LIMIT 9 OFFSET :offset;";
		List<Item> items = namedParameterJdbcTemplate.query(sql, param, ITEM_ROW_MAPPER);
		return items;
	}
	
	public Integer countAllItem () {
		SqlParameterSource param = new MapSqlParameterSource();
		String sql = "SELECT COUNT(*) FROM items;";
		Integer countItem = namedParameterJdbcTemplate.queryForObject(sql, param, Integer.class);
		return countItem;
	}
	


}
