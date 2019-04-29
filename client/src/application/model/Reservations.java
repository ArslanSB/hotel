package application.model;

import java.sql.Date;

public class Reservations {

	private int id_client;
	private int id_apartment;
	private int id;
	private String card_type;
	private String card_number;
	private String card_expiredate;
	private String status;
	private Double total;
	private Date payment_day;
	private Date res_start;
	private Date res_end;
	private String client_name;
	
	public int getId_client() {
		return id_client;
	}

	public void setId_client(int id_client) {
		this.id_client = id_client;
	}

	public int getId_apartment() {
		return id_apartment;
	}

	public void setId_apartment(int id_apartment) {
		this.id_apartment = id_apartment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public String getCard_expiredate() {
		return card_expiredate;
	}

	public void setCard_expiredate(String card_expiredate) {
		this.card_expiredate = card_expiredate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Date getPayment_day() {
		return payment_day;
	}

	public void setPayment_day(Date payment_day) {
		this.payment_day = payment_day;
	}

	public Date getRes_start() {
		return res_start;
	}

	public void setRes_start(Date res_start) {
		this.res_start = res_start;
	}

	public Date getRes_end() {
		return res_end;
	}

	public void setRes_end(Date res_end) {
		this.res_end = res_end;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	public Reservations(
			
			int id_client,
			int id_apartment,
			int id,
			String card_type,
			String card_number,
			String card_expiredate,
			String status,
			Double total,
			Date payment_day,
			Date res_start,
			Date res_end,
			String client_name
			
			) {
		
		this.id_client = id_client;
		this.id_apartment = id_apartment;
		this.id = id;
		this.card_type = card_type;
		this.card_number = card_number;
		this.card_expiredate = card_expiredate;
		this.status = status;
		this.total = total;
		this.payment_day = payment_day;
		this.res_start = res_start;
		this.res_end = res_end;
		this.client_name = client_name;
		
	}
	
}
