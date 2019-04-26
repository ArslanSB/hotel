package application.model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.bind.DatatypeConverter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Database {
	
	private static Database _instance = null;
	private String error = "";
	private Connection connection = null;
	private HotelConfigProperties config = HotelConfigProperties.getInstance();
	private UsefullFunctions uff = UsefullFunctions.getInstance();
	
	private Database() {
		try {
			String dbhost = config.getProperties().getProperty("dbhost");
			String dbuser = config.getProperties().getProperty("dbuser");
			String dbpassword = config.getProperties().getProperty("dbpassword");
			String dbname = config.getProperties().getProperty("dbname");
			
			connection = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			uff.showAlerts("Could not connect to the database", "error");
			e.printStackTrace();
		}
		
	}
	
	public static Database getInstance() {
		Database._instance = (Database._instance == null) ? new Database() : Database._instance;
		return Database._instance;
	}
	
	public boolean checkLogin(String username, String password, boolean rememberme) {
		
		boolean userFound = false;
		try {
			Statement query = this.connection.createStatement();
			ResultSet results = query.executeQuery("SELECT * FROM clients WHERE (username like '" + username + "' or email like '" + username + "') AND password like '" + password + "';");
			if(results.next()) {
				userFound = true;
				Client.setLoggedInUser(createClient(results));	    		
	    		if(rememberme) {
	    			config.setProperty("user", Client.getLoggedInUser().getUsername());
		    		config.setProperty("id", String.valueOf(Client.getLoggedInUser().getId()));
		    		config.setProperty("expire", Client.getLoggedInUser().getRemberme());
	    			updateClientWithSession(Client.getLoggedInUser().getUsername(), Client.getLoggedInUser().getId(), Client.getLoggedInUser().getRemberme());
	    		}
				uff.showAlerts("Logged in successfully...", "ok");
	    		
			}else {
				uff.showAlerts("Username/Email or password incorrect...", "error");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			uff.showAlerts("Something went wrong with the database...", "error");
		}
		return userFound;
	}
	
	private void updateClientWithSession(String username, int id, String remberme) {
		
		String query = "UPDATE clients SET remember = '" + remberme + "' WHERE id = " + id + " AND username like '" + username + "';";
		try {
			Statement stmt = this.connection.createStatement();
			int results = stmt.executeUpdate(query);
			
			if(results > 0) {
				uff.showAlerts("User updated successfully...", "ok");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			uff.showAlerts("Something went wrong with the database...", "error");
		}
		
	}

	public String getHashedPassword(String plainText) {
		String pass = "";
		
		try {
			MessageDigest md5Password = MessageDigest.getInstance("MD5");
			md5Password.reset();
			md5Password.update(plainText.getBytes("UTF-8"));
			byte[] result = md5Password.digest();
			pass = DatatypeConverter.printHexBinary(result).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pass;
	}
	
	public ObservableList<Client> getClients(String filter) {
			
		ObservableList<Client> clients = FXCollections.observableArrayList();

		// CONCAT(COALESCE(name,""), " ",COALESCE(surnames,""), " ",COALESCE(address,""), " ",COALESCE(zipcode,""), " ",COALESCE(telephone,""), " ",email, " ",username)
		// REGEXP 'condition1|condition2|...'
		
		String finalFilter = "";
		String[] filterArray = filter.split(" ");
		
		String query = "SELECT * FROM clients";
		if(filterArray[0].length() > 0 ) {
			for(String f : filterArray) {
				finalFilter += f + "|";
			}
			
			finalFilter = finalFilter.substring(0, finalFilter.length() - 1);
			query += " WHERE CONCAT(email, \" \",username) REGEXP ?";
		}
		
		try {
			PreparedStatement prepare = this.connection.prepareStatement(query);
			if(filterArray[0].length() > 0) {
				prepare.setString(1, finalFilter);
			}
			ResultSet results = prepare.executeQuery();
			while(results.next()) {
				clients.add(createClient(results));
			}
		} catch (SQLException e) {
			uff.showAlerts("Something went wrong with the database...", "error");
		}
				
		return clients;
		
	}
	
	public void updateClient(
			String id,
			String username,
			String password,
			String email,
			String access_type
	) {
		
		String query = "UPDATE clients SET username = '" + username + "', email = '" + email + "', access_type = '" + access_type + "'";
		if(!password.equals("***")) {
			String pass = this.getHashedPassword(password);
			query += ", password = '" + pass + "'";
		}
		query += " WHERE id like '" + id + "';";
		
		try {
			Statement stmt = this.connection.createStatement();
			int results = stmt.executeUpdate(query);
			
			if(results > 0) {
				System.out.println("User updated successfully....");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			uff.showAlerts("Something went wrong with the database...", "error");
		}
		
	}
		
	public String getError() {
		return this.error;
	}

	public boolean checkUserByExpireDate(String configUser, String configId, String configExpire) {
		
		boolean userFound = false;
		try {
			Statement query = this.connection.createStatement();
			ResultSet results = query.executeQuery("SELECT * FROM clients WHERE (username like '" + configUser + "' OR email like '" + configUser + "') AND id =" + Integer.parseInt(configId) + " AND remember like '" + configExpire + "';");
			if(results.next()) {
				userFound = true;
				Client.setLoggedInUser(createClient(results));
				uff.showAlerts("Logged in successfully...", "ok");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			uff.showAlerts("Something went wrong with the database...", "error");
		}
		return userFound;
		
	}

	public boolean sendRecoveryCode(String username) {
		
		int randomToken = (int) Math.floor(Math.random() * 999999) + 100000;
		
		boolean sent = false;
		
		try {
			Statement stmt = this.connection.createStatement();
			int results = stmt.executeUpdate("UPDATE clients SET recoverCode = " + randomToken + " WHERE (username like '" + username + "' or email like '" + username + "');");
			
			if(results > 0) {
				
				try {
					ResultSet emailRes = stmt.executeQuery("SELECT email FROM clients WHERE (username like '" + username + "' or email like '" + username + "');");
					
					if(emailRes.next()) {
						String email = emailRes.getString("email");
						if(Email.getInstance().sendEmail(email, "Recovery code", "Here is your recovery code: " + randomToken + ", please put this code in the 'Recovery Code' field.")) {
							uff.showAlerts("Recovery code sent to your email...", "ok");
							sent = true;
						}else {
							uff.showAlerts("Found an error while sending the recovry code...", "error");
						}
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					uff.showAlerts("Something went wrong with the database...", "error");
				}
			}else {
				uff.showAlerts("Found an error while sending the recovry code...", "error");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			uff.showAlerts("Found an error while sending the recovry code...", "error");
		}
		
		return sent;
	}

	public boolean checkRecoveryCode(String username, String recovyCode) {
		
		boolean userFound = false;
		try {
			Statement query = this.connection.createStatement();
			ResultSet results = query.executeQuery("SELECT * FROM clients WHERE (username like '" + username + "' OR email like '" + username + "') AND recoverCode =" + Integer.parseInt(recovyCode) + ";");
			if(results.next()) {
				userFound = true;
				uff.showAlerts("Recovery code is correct, please choose your new password...", "ok");
			}else {
				uff.showAlerts("Recovery code is incorrect, please check again...", "error");
			}
		} catch (SQLException e) {
			uff.showAlerts("Something went wrong with the database...", "error");
		}
		return userFound;
		
	}

	public boolean updateUserPassword(String username, String password) {
		
		boolean updated = false;
		
		String pass = getHashedPassword(password);
		
		String query = "UPDATE clients SET password = '" + pass + "' WHERE (username like '" + username + "' or email like '" + username + "');";
		
		try {
			Statement stmt = this.connection.createStatement();
			int results = stmt.executeUpdate(query);
			
			if(results > 0) {
				updated = true;
				uff.showAlerts("Your password has been updated successfully!", "ok");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			uff.showAlerts("Something went wrong with the database...", "error");
		}
		
		return updated;
	}

	public boolean addnewUser(String username, String password, String email) {
		
		boolean userAdded = false;
		
		String query = "INSERT INTO clients (username, password, email, access_type) VALUES ('" + username + "', '" + password + "', '" + email + "', 'user');";
		try {
			Statement stmt = this.connection.createStatement();
			int results = stmt.executeUpdate(query);
			
			if(results > 0) {
				userAdded = true;
				uff.showAlerts("User has been added successfully!", "ok");
			}else {
				uff.showAlerts("Could not add new user for some reason...", "error");
			}
		}catch(Exception e) {
			uff.showAlerts("Something went wrong with the database...", "error");
			e.printStackTrace();
		}
		
		return userAdded;
	}

	public void signOut() {

		config.setProperty("user", "");
		config.setProperty("id", "");
		config.setProperty("expire", "");
		
		uff.changeScene("../view/Login.fxml", "Login");
	}

	private Client createClient(ResultSet results) throws SQLException {
		
		return new Client(
				results.getInt("id"),
				results.getString("name"),
				results.getString("surnames"),
				results.getString("address"),
				results.getString("zipcode"),
				results.getDate("dateofbirth"),
				results.getString("telephone"),
				results.getString("username"),
				"***",
				results.getString("email"),
				results.getString("access_type")
		);
		
	}
	
}