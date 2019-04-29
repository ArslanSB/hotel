package application.model;

public class Apartment {
	
	private int id;
	private int num_rooms;
	private int max_capacity;
	private String description;
	private String address;
	private int id_client;
	private double price_night;
	private String state;
	private String client_name;
	
	public Apartment(
			int id,
			int num_rooms,
			int max_capacity,
			String description,
			String address,
			int id_client,
			double price_night,
			String state,
			String client_name
			) {
		
		this.id = id;
		this.num_rooms = num_rooms;
		this.max_capacity = max_capacity;
		this.description = description;
		this.address = address;
		this.id_client = id_client;
		this.price_night = price_night;
		this.state = state;
		this.client_name = client_name;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNum_rooms() {
		return num_rooms;
	}

	public void setNum_rooms(int num_rooms) {
		this.num_rooms = num_rooms;
	}

	public int getMax_capacity() {
		return max_capacity;
	}

	public void setMax_capacity(int max_capacity) {
		this.max_capacity = max_capacity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getId_client() {
		return id_client;
	}

	public void setId_client(int id_client) {
		this.id_client = id_client;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}
	
	public double getPrice_night() {
		return price_night;
	}

	public void setPrice_night(double price_night) {
		this.price_night = price_night;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}
