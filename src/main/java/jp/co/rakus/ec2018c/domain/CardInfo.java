package jp.co.rakus.ec2018c.domain;

public class CardInfo {
	
	private Integer user_id;
	private long order_number;
	private Integer amount;
	private long card_number;
	
	private Integer card_exp_year;
	private Integer card_exp_month;
	
	
	private String card_name;

	private Integer card_cvv;
	
	
	@Override
	public String toString() {
		return "CardInfo [user_id=" + user_id + ", order_number=" + order_number + ", amount=" + amount
				+ ", card_number=" + card_number + ", card_exp_year=" + card_exp_year + ", card_exp_month="
				+ card_exp_month + ", card_name=" + card_name + ", card_cvv=" + card_cvv + "]";
	}


	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public long getOrder_number() {
		return order_number;
	}

	public void setOrder_number(long order_number) {
		this.order_number = order_number;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public long getCard_number() {
		return card_number;
	}

	public void setCard_number(long card_number) {
		this.card_number = card_number;
	}

	public Integer getCard_exp_year() {
		return card_exp_year;
	}

	public void setCard_exp_year(Integer card_exp_year) {
		this.card_exp_year = card_exp_year;
	}

	public Integer getCard_exp_month() {
		return card_exp_month;
	}

	public void setCard_exp_month(Integer card_exp_month) {
		this.card_exp_month = card_exp_month;
	}

	public String getCard_name() {
		return card_name;
	}

	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	public Integer getCard_cvv() {
		return card_cvv;
	}

	public void setCard_cvv(Integer card_cvv) {
		this.card_cvv = card_cvv;
	}







}
