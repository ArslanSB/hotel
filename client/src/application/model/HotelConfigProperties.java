package application.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;


public class HotelConfigProperties {

	private URL resourceLink = Main.class.getResource("/application/resources/config.properties");
	private File resourceFile = null;
	private Properties props = null;
	private static HotelConfigProperties _instance = null;
	
	private HotelConfigProperties() {
		try {
			resourceFile = new File(resourceLink.toURI());
		} catch (URISyntaxException e) {
			System.err.println("Found an error while loading properties file...");
		}
		props = new Properties();
		loadProperties();
	}
	
	public void loadProperties() {
		
		InputStream input = null;
		try {
			input = new FileInputStream(resourceFile);
			props.load(input);
		} catch (Exception e) {
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
		HotelConfigProperties._instance = (HotelConfigProperties._instance == null) ? new HotelConfigProperties() : HotelConfigProperties._instance;
		return HotelConfigProperties._instance;
	}
	
	public Properties getProperties() {
		return this.props;
	}
	
	public void setProperty(String key, String value) {
		
		this.props.setProperty(key, value);
		
		try {
			FileOutputStream save = new FileOutputStream(resourceFile);
			props.store(save, "Property " + key + " saved!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Found an error while saving the property....");
		}
		
	}
	
}
