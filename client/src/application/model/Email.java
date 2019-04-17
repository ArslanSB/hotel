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

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;

public class Email {

	private static Email _instance;
	private HotelConfigProperties config = HotelConfigProperties.getInstance();
	private UsefullFunctions uff = UsefullFunctions.getInstance();
	private String username = config.getProperties().getProperty("gmail.username");
	private String password = config.getProperties().getProperty("gmail.password");
	private Properties prop = null;
	private Session session = null;
	
	private Message message = null;
	
	private Email() {
		prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		
		session = Session.getInstance(prop,
				new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
		                return new PasswordAuthentication(username, password);
		            }
				});
		try {
			message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Email getInstance() {
		Email._instance = (Email._instance == null) ? new Email() : Email._instance;
		return Email._instance;
	}
	
	public boolean sendEmail( String email, String sub, String msg ) {
		
		boolean sent = false;
		
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			
			message.setSubject(sub);
			message.setContent(msg, "text/html");
			
			Transport.send(message);
			sent = true;
			uff.showAlerts(FontAwesomeIcon.CHECK, "Email sent successfully, please check your email...", "ok");
		}catch(MessagingException e) {
			uff.showAlerts(FontAwesomeIcon.CLOSE, "Found an error while sending email...", "error");
		}
		
		return sent;
		
	}	
}
