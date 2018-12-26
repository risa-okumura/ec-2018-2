package jp.co.rakus.ec2018c.form;

import javax.validation.constraints.NotEmpty;

/**
 * 商品のリクエストパラメータを受け取るフォーム.
 * 
 * @author momo.senda
 *
 */
public class SearchItemForm {
	/**商品名*/
	@NotEmpty(message="入力してください")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
