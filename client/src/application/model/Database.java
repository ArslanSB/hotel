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

public class Database {
	
	private static Database _instance = null;
	private String error = "";
	private Connection connection = null;
	
	private Database() {
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/testing", "testing", "testing");
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
			ResultSet results = query.executeQuery("SELECT * FROM clientes WHERE username like '" + username + "' AND password like '" + password + "';");
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
		
	public String getError() {
		return this.error;
	}
}