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


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Database {
	
	private static Database _instance = null;
	private String error = "";
	private Connection connection = null;
	private HotelConfigProperties config = HotelConfigProperties.getInstance();
	
	private Database() {
		try {
			String dbhost = config.getProperties().getProperty("dbhost");
			String dbuser = config.getProperties().getProperty("dbuser");
			String dbpassword = config.getProperties().getProperty("dbpassword");
			String dbname = config.getProperties().getProperty("dbname");
			
			connection = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.error = "Could not connect to the database...";
			System.out.println("Error: " + this.error);
		}
		
	}
	
	public static Database getInstance() {
		return (Database._instance == null) ? new Database() : Database._instance;
	}
	
	public boolean checkLogin(String username, String password) {
		
		boolean userFound = false;
		try {
			Statement query = this.connection.createStatement();
			ResultSet results = query.executeQuery("SELECT * FROM clients WHERE username like '" + username + "' AND password like '" + password + "';");
			if(results.next()) {
				userFound = true;
			}else {
				this.error = "Username or password incorrect...";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.error = "Error found during the sql statement...";
			System.out.println("Error: " + this.error);
		}
		return userFound;
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
			ResultSet results = query.executeQuery("SELECT * FROM clientes;");
			if(results.next()) {
				clients.add(new Client(results.getInt("id"), results.getString("username"), "***", results.getString("email"), results.getString("access_type")));
			}else {
				this.error = "No clients found....";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.error = "Error found during the sql statement...";
			System.out.println("Error: " + this.error);
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
		
		String query = "UPDATE clientes SET username = '" + username + "', email = '" + email + "', access_type = '" + access_type + "'";
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
			this.error = "Error found during the sql statement...";
			System.out.println("Error: " + this.error);
		}
		
	}
	
	public void insertClient(
			String username,
			String password,
			String email
	) {
		
		String pass = this.getHashedPassword(password);
		String query = "INSERT INTO clientes (username, password, email, access_type) VALUES ('" + username + "', '" + pass + "', '" + email + "', 'receptionist');";
		
		try {
			Statement stmt = this.connection.createStatement();
			int results = stmt.executeUpdate(query);
			
			if(results > 0) {
				System.out.println("User inserted successfully....");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.error = "Error found during the sql statement...";
			System.out.println("Error: " + this.error);
		}
		
	}
	
	public String getError() {
		return this.error;
	}
}