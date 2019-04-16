package application.model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.bind.DatatypeConverter;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
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
			uff.showAlerts(FontAwesomeIcon.CLOSE, "Could not connect to the database", "error");
		}
		
	}
	
	public static Database getInstance() {
		return (Database._instance == null) ? new Database() : Database._instance;
	}
	
	public boolean checkLogin(String username, String password, boolean rememberme) {
		
		boolean userFound = false;
		try {
			Statement query = this.connection.createStatement();
			ResultSet results = query.executeQuery("SELECT * FROM clients WHERE (username like '" + username + "' or email like '" + username + "') AND password like '" + password + "';");
			if(results.next()) {
				userFound = true;
				Client.setLoggedInUser(new Client(results.getInt("id"), results.getString("username"), "***", results.getString("email"), results.getString("access_type")));	    		
	    		if(rememberme) {
	    			config.setProperty("user", Client.getLoggedInUser().getUsername());
		    		config.setProperty("id", String.valueOf(Client.getLoggedInUser().getId()));
		    		config.setProperty("expire", Client.getLoggedInUser().getRemberme());
	    			updateClientWithSession(Client.getLoggedInUser().getUsername(), Client.getLoggedInUser().getId(), Client.getLoggedInUser().getRemberme());
	    		}
				uff.showAlerts(FontAwesomeIcon.CHECK, "Logged in successfully...", "ok");
	    		
			}else {
				uff.showAlerts(FontAwesomeIcon.CLOSE, "Username/Email or password incorrect...", "error");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			uff.showAlerts(FontAwesomeIcon.CLOSE, "Something wen't wrong with the database...", "error");
		}
		return userFound;
	}
	
	private void updateClientWithSession(String username, int id, String remberme) {
		
		String query = "UPDATE clients SET remember = '" + remberme + "' WHERE id = " + id + " AND username like '" + username + "';";
		try {
			Statement stmt = this.connection.createStatement();
			int results = stmt.executeUpdate(query);
			
			if(results > 0) {
				uff.showAlerts(FontAwesomeIcon.CHECK, "User updated successfully...", "ok");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			uff.showAlerts(FontAwesomeIcon.CLOSE, "Something wen't wrong with the database...", "error");
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
	
	public ObservableList<Client> allClients() {
		
		ObservableList<Client> clients = FXCollections.observableArrayList();
		try {
			Statement query = this.connection.createStatement();
			ResultSet results = query.executeQuery("SELECT * FROM clients;");
			if(results.next()) {
				clients.add(new Client(results.getInt("id"), results.getString("username"), "***", results.getString("email"), results.getString("access_type")));
			}else {
				this.error = "No clients found....";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			uff.showAlerts(FontAwesomeIcon.CLOSE, "Something wen't wrong with the database...", "error");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			uff.showAlerts(FontAwesomeIcon.CLOSE, "Something wen't wrong with the database...", "error");
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
				uff.showAlerts(FontAwesomeIcon.CHECK, "Logged in successfully...", "ok");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			uff.showAlerts(FontAwesomeIcon.CLOSE, "Something wen't wrong with the database...", "error");
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
							uff.showAlerts(FontAwesomeIcon.CHECK, "Recovery code sent to your email...", "ok");
							sent = true;
						}else {
							uff.showAlerts(FontAwesomeIcon.CLOSE, "Found an error while sending the recovry code...", "error");
						}
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					uff.showAlerts(FontAwesomeIcon.CLOSE, "Something wen't wrong with the database...", "error");
				}
			}else {
				uff.showAlerts(FontAwesomeIcon.CLOSE, "Found an error while sending the recovry code...", "error");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			uff.showAlerts(FontAwesomeIcon.CLOSE, "Found an error while sending the recovry code...", "error");
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
				uff.showAlerts(FontAwesomeIcon.CHECK, "Recovery code is correct, please choose your new password...", "ok");
			}else {
				uff.showAlerts(FontAwesomeIcon.CLOSE, "Recovery code is incorrect, please check again...", "error");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			uff.showAlerts(FontAwesomeIcon.CLOSE, "Something wen't wrong with the database...", "error");
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
				uff.showAlerts(FontAwesomeIcon.CHECK, "Your password has been updated successfully!", "ok");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			uff.showAlerts(FontAwesomeIcon.CLOSE, "Something wen't wrong with the database...", "error");
		}
		
		return updated;
	}

}