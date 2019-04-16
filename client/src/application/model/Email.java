package application.model;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

	private static Email _instance;
	private HotelConfigProperties config = HotelConfigProperties.getInstance();
	
	private Email() { }
	
	public static Email getInstance() {
		return (Email._instance == null) ? new Email() : Email._instance;
	}
	
	public boolean sendEmail( String email, String sub, String msg ) {
		
		boolean sent = false;
		
		String username = config.getProperties().getProperty("gmail.username");
		String password = config.getProperties().getProperty("gmail.password");
		
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		
		Session session = Session.getInstance(prop,
				new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
		                return new PasswordAuthentication(username, password);
		            }
				});
		
		try {
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			
			message.setSubject(sub);
			message.setText(msg);
			
			Transport.send(message);
			sent = true;
		}catch(MessagingException e) {
			System.err.println("Found an error while sending email...");
		}
		
		return sent;
		
	}
	
}
