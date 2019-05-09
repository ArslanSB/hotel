package application.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class HotelConfigProperties {

	private String resourceRoute = null;
	private Properties props = null;
	private static HotelConfigProperties _instance = null;
	
	private HotelConfigProperties() {
		try {
			resourceRoute = new File("resources/config.properties").getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		props = new Properties();
		loadProperties();
	}
	
	public void loadProperties() {
		
		InputStream input = null;
		try {
			input = new FileInputStream(new File(resourceRoute));
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
			FileOutputStream save = null;
			save = new FileOutputStream(new File(resourceRoute));
			props.store(save, "Property " + key + " saved!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Found an error while saving the property....");
		}
		
	}
	
}
