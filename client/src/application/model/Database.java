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
import java.time.LocalDate;
import java.util.ArrayList;

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
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
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
			e.printStackTrace();
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
					e.printStackTrace();
					uff.showAlerts("Something went wrong with the database...", "error");
				}
			}else {
				uff.showAlerts("Found an error while sending the recovry code...", "error");
			}
			
		} catch (SQLException e) {
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

	public ObservableList<Apartment> getApartments() {
		ObservableList<Apartment> apartments = FXCollections.observableArrayList();
		
		try {
			Statement query = this.connection.createStatement();
			ResultSet results = query.executeQuery("SELECT a.*, CONCAT(c.name, \" \", c.surnames) as \"client_name\", CASE WHEN (CURRENT_DATE BETWEEN r.res_start AND r.res_end) THEN \"BUSY\" ELSE \"FREE\" END as \"Status\" FROM (apartments a LEFT JOIN clients c on a.id_client = c.id) LEFT JOIN reservations r on a.id = r.id_apartment;");
			while(results.next()) {
				apartments.add(new Apartment(
						results.getInt("id"),
						results.getInt("num_rooms"),
						results.getInt("max_capacity"),
						results.getString("description"),
						results.getString("address"),
						results.getInt("id_client"),
						results.getDouble("price_night"),
						results.getString("state").equalsIgnoreCase("") || results.getString("state") == null ? results.getString("Status") : results.getString("state"),
						results.getString("client_name")
						));
			}
		} catch (SQLException e) {
			uff.showAlerts("Something went wrong with the database...", "error");
		}
		
		return apartments;
	}

	public ObservableList<Apartment> getApartmentsByReservationDate(LocalDate startDate, LocalDate endDate) {
		/*
		 * 	SET @start = '2019-04-20';
			SET @end = '2019-05-01';
			SELECT a.*, r.res_start, r.res_end, @start as "Start", @end as "End" FROM apartments a left JOIN reservations r on a.id = r.id_apartment WHERE r.res_start is null OR (
				(r.res_start NOT BETWEEN @start AND @end) AND
			    (r.res_end NOT BETWEEN @start AND @end) AND
			    (@start NOT BETWEEN r.res_start AND r.res_end) AND 
			    (@end NOT BETWEEN r.res_start AND r.res_end)
			)
		 */
		
		String sd = startDate.toString();
		String ed = endDate.toString();
		ObservableList<Apartment> apartments = FXCollections.observableArrayList();
		
		try {
			Statement query = this.connection.createStatement();
			ResultSet results = query.executeQuery("SELECT a.* FROM apartments a LEFT JOIN reservations r on a.id = r.id_apartment WHERE r.res_start is null OR (\n" + 
					"				(r.res_start NOT BETWEEN '"+sd+"' AND '"+ed+"') AND\n" + 
					"			    (r.res_end NOT BETWEEN '"+sd+"' AND '"+ed+"') AND\n" + 
					"			    ('"+sd+"' NOT BETWEEN r.res_start AND r.res_end) AND \n" + 
					"			    ('"+ed+"' NOT BETWEEN r.res_start AND r.res_end)\n" + 
					"			);");
			while(results.next()) {
				apartments.add(new Apartment(
						results.getInt("id"),
						results.getInt("num_rooms"),
						results.getInt("max_capacity"),
						results.getString("description"),
						results.getString("address"),
						results.getInt("id_client"),
						results.getDouble("price_night"),
						results.getString("state"),
						""
						));
			}
		} catch (SQLException e) {
			uff.showAlerts("Something went wrong with the database...", "error");
			e.printStackTrace();
		}
		
		return apartments;
	}
	
	public void deleteApartment(Apartment apartment) {
		
		String query = "DELETE FROM apartments WHERE id = " + apartment.getId() + ";";
		try {
			Statement stmt = this.connection.createStatement();
			int results = stmt.executeUpdate(query);
			
			if(results > 0) {
				uff.showAlerts("Apartment has been deleted successfully!", "ok");
			}
		}catch(Exception e) {
			uff.showAlerts("Something went wrong with the database...", "error");
			e.printStackTrace();
		}
		
	}

	public ObservableList<Reservations> getReservations(String start_date, String end_date) {
		
		/*
		 * 	SET @start = '2019-04-20';
			SET @end = '2019-05-01';
			SELECT a.*, r.res_start, r.res_end, @start as "Start", @end as "End" FROM apartments a left JOIN reservations r on a.id = r.id_apartment WHERE r.res_start is null OR (
				(r.res_start NOT BETWEEN @start AND @end) AND
			    (r.res_end NOT BETWEEN @start AND @end) AND
			    (@start NOT BETWEEN r.res_start AND r.res_end) AND 
			    (@end NOT BETWEEN r.res_start AND r.res_end)
			)
		 */
		
		ObservableList<Reservations> reservations = FXCollections.observableArrayList();
		String queryStmt = "SELECT r.*, CONCAT(c.name, \" \", c.surnames) as \"client_name\" FROM reservations r INNER JOIN clients c on r.id_client = c.id WHERE ";
		if(!start_date.equalsIgnoreCase("") && !end_date.equalsIgnoreCase("")) {
			queryStmt += " (r.res_start BETWEEN '"+start_date+"' AND '"+end_date+"') OR\n" + 
					"			    (r.res_end BETWEEN '"+start_date+"' AND '"+end_date+"') OR\n" + 
					"			    ('"+start_date+"' BETWEEN r.res_start AND r.res_end) OR \n" + 
					"			    ('"+end_date+"' BETWEEN r.res_start AND r.res_end) AND ";
		}else {
			if(!start_date.equalsIgnoreCase("")) {
				queryStmt += " res_start >= '" + start_date + "' AND ";
			}
			
			if(!end_date.equalsIgnoreCase("")) {
				queryStmt += " '" + end_date + "' <= res_end AND ";
			}
		}
		
		if(Client.getLoggedInUser().getAccess_type().equalsIgnoreCase("user")) {
			queryStmt += " id_client = " + Client.getLoggedInUser().getId() + " ";
		}else {
			queryStmt += " id_client like '%'";
		}
		try {
			Statement query = this.connection.createStatement();
			ResultSet results = query.executeQuery(queryStmt);
			while(results.next()) {
				reservations.add(new Reservations(
						results.getInt(1),
						results.getInt(2),
						results.getInt(3),
						results.getString(4),
						results.getString(5),
						results.getString(6),
						results.getString(7),
						results.getDouble(8),
						results.getDate(9),
						results.getDate(10),
						results.getDate(11),
						results.getString(12)
						));
			}
		} catch (SQLException e) {
			uff.showAlerts("Something went wrong with the database...", "error");
			e.printStackTrace();
		}
		
		return reservations;
	}

	public void deleteUser(Client client) {
		String query = "DELETE FROM clients WHERE id = " + client.getId() + ";";
		try {
			Statement stmt = this.connection.createStatement();
			int results = stmt.executeUpdate(query);
			
			if(results > 0) {
				uff.showAlerts("Client has been deleted successfully!", "ok");
			}
		}catch(Exception e) {
			uff.showAlerts("Something went wrong with the database...", "error");
			e.printStackTrace();
		}
	}

	public boolean addnewUser(Client client) {
		
		boolean userAdded = false;
		
		String query = "INSERT INTO clients (name, surnames, address, zipcode, telephone, dateofbirth, username, password, email) VALUES "
				+ "('" + client.getName() + "', '" + client.getSurnames() + "', '" + client.getAddress() + "', '" + client.getZipcode() + "', '" + client.getTelephone() + "', '" + client.getDateofbirth() + "', '" + client.getUsername() + "', '" + client.getPassword() + "', '" + client.getEmail() + "');";
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

	public boolean updateClient(Client client) {
		
		String updateTheseProperties = "username='"+client.getUsername()+"', email='"+client.getEmail()+"', dateofbirth='"+client.getDateofbirth()+"', ";
		
		if(!client.getName().equalsIgnoreCase("") || !client.getName().equalsIgnoreCase("NA")) { updateTheseProperties += " name='"+client.getName()+"', "; }
		if(!client.getSurnames().equalsIgnoreCase("") || !client.getSurnames().equalsIgnoreCase("NA")) { updateTheseProperties += " surnames='"+client.getSurnames()+"', "; }
		if(!client.getAddress().equalsIgnoreCase("") || !client.getAddress().equalsIgnoreCase("NA")) { updateTheseProperties += " address='"+client.getAddress()+"', "; }
		if(!client.getZipcode().equalsIgnoreCase("") || !client.getZipcode().equalsIgnoreCase("NA")) { updateTheseProperties += " zipcode='"+client.getZipcode()+"', "; }
		if(!client.getTelephone().equalsIgnoreCase("") || !client.getTelephone().equalsIgnoreCase("NA")) { updateTheseProperties += " telephone='"+client.getTelephone()+"', "; }
		if(!client.getPassword().equalsIgnoreCase("***")) { updateTheseProperties += " password='"+client.getPassword()+"', "; }
		
		updateTheseProperties = updateTheseProperties.substring(0, updateTheseProperties.length() - 2);
		
		boolean userAdded = false;
		
		String query = "UPDATE clients SET " + updateTheseProperties + " WHERE id = " + client.getId() + ";";
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

	public boolean addApartment(Apartment apartment) {
		
		boolean userAdded = false;
		
		String query = "INSERT INTO apartments (num_rooms, max_capacity, description, address, id_client, price_night) VALUES "
				+ "("+ apartment.getNum_rooms() +","+apartment.getMax_capacity()+",'"+apartment.getDescription()+"','"+apartment.getAddress()+"',"+ apartment.getId_client() +", "+ apartment.getPrice_night() +");";
		try {
			Statement stmt = this.connection.createStatement();
			int results = stmt.executeUpdate(query);
			
			if(results > 0) {
				userAdded = true;
				uff.showAlerts("Apartment has been added successfully!", "ok");
			}else {
				uff.showAlerts("Could not add new apartment for some reason...", "error");
			}
		}catch(Exception e) {
			uff.showAlerts("Something went wrong with the database...", "error");
			e.printStackTrace();
		}
		
		return userAdded;
		
	}

	public boolean updateApartment(Apartment old, Apartment newApartment) {
		
		String query = "UPDATE apartments SET ";
		if(old.getNum_rooms() != newApartment.getNum_rooms()) { query += " num_rooms=" + newApartment.getNum_rooms() + ", "; }
		if(old.getMax_capacity() != newApartment.getMax_capacity() ) { query += " max_capacity=" + newApartment.getMax_capacity() + ", "; }
		if(!old.getDescription().equalsIgnoreCase(newApartment.getDescription())) { query += " description='" + newApartment.getDescription() + "', "; }
		if(!old.getAddress().equalsIgnoreCase(newApartment.getAddress())) { query += " address='" + newApartment.getAddress() + "', "; }
		if(old.getId_client() != newApartment.getId_client()) { query += " id_client=" + newApartment.getId_client() + ", "; }
		if(old.getPrice_night() != newApartment.getPrice_night()) { query += " price_night=" + newApartment.getPrice_night() + ", "; }
		if(old.getState() != newApartment.getState()) { query += " state='" + newApartment.getState() + "', "; }
		
		query = query.substring(0, query.length() - 2);
		query += " WHERE id = " + old.getId();
				
		boolean userAdded = false;
		
		try {
			Statement stmt = this.connection.createStatement();
			int results = stmt.executeUpdate(query);
			
			if(results > 0) {
				userAdded = true;
				uff.showAlerts("Apartment has been updated successfully!", "ok");
			}else {
				uff.showAlerts("Could not update apartment for some reason...", "error");
			}
		}catch(Exception e) {
			uff.showAlerts("Something went wrong with the database...", "error");
			e.printStackTrace();
		}
		
		return userAdded;
		
	}
	
	public Client findUserById(int id_client) {
		Client c = null;
		
		try {
			Statement query = this.connection.createStatement();
			ResultSet results = query.executeQuery("SELECT * FROM clients WHERE id like " + id_client + ";");
			if(results.next()) {
				c = createClient(results);
			}
		} catch (SQLException e) {
			uff.showAlerts("Something went wrong with the database...", "error");
		}
		
		return c;
	}

	public boolean addReservation(Reservations r) {
		
		String query = "INSERT INTO reservations (id_client, id_apartment, card_type, card_number, card_expiredate, status, total, payment_day, res_start, res_end) VALUES ("
				+ r.getId_client() + ", " + r.getId_apartment() + ", '" + r.getCard_type() + "', '"+ r.getCard_number() +"', '"+ r.getCard_expiredate() +"', '" + r.getStatus() + "', " + r.getTotal() + ", " + ((r.getPayment_day() == null) ? "NULL" : ("'" + r.getPayment_day() + "'"))  + ", '" + r.getRes_start() + "', '" + r.getRes_end() + "');";
		boolean resAdded = false;
		try {
			Statement stmt = this.connection.createStatement();
			int results = stmt.executeUpdate(query);
			
			if(results > 0) {
				resAdded = true;
				uff.showAlerts("Reservation has been added successfully!", "ok");
			}else {
				uff.showAlerts("Could not add reservation for some reason...", "error");
			}
		}catch(Exception e) {
			uff.showAlerts("Something went wrong with the database...", "error");
			e.printStackTrace();
		}
		
		return resAdded;
	}
	
}