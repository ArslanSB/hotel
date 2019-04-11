package application.model;

public class Client {

	private int id;
	private String username;
	private String password;
	private String email;
	private String access_type;
	
	public Client(
			int id,
			String username,
			String password,
			String email,
			String access_type
	) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.access_type = access_type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccess_type() {
		return access_type;
	}

	public void setAccess_type(String access_type) {
		this.access_type = access_type;
	}
	
}
