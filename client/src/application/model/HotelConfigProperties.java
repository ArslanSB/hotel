package application.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class HotelConfigProperties {

	private String resourceLink = new File("./src/application/resources/config.properties").getAbsolutePath();
	private Properties props = null;
	private static HotelConfigProperties _instance = null;
	
	private HotelConfigProperties() {
		props = new Properties();
		loadProperties();
	}
	
	public void loadProperties() {
		
		InputStream input = null;
		try {
			input = new FileInputStream(this.resourceLink);
			props.load(input);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Found an error while loading properties file...");
		} finally {
			if( input != null ) {
				try {
					input.close();
				}catch(IOException ex) {
					System.err.println("Found an error while closing the input stream...");
				}
			}
		}
	}
	
	public static HotelConfigProperties getInstance() {
		return (HotelConfigProperties._instance == null) ? new HotelConfigProperties() : HotelConfigProperties._instance;
	}
	
	public Properties getProperties() {
		return this.props;
	}
	
	public void setProperty(String key, String value) {
		
		this.props.setProperty(key, value);
		
		try {
			FileOutputStream save = new FileOutputStream(resourceLink);
			props.store(save, "Property " + key + " saved!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Found an error while saving the property....");
		}
		
	}
	
}
