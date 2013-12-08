/**
 * 
 */
package server.serverinterface.emailinterface;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import server.control.errormanagement.ErrorManager;



/**
 * Sends out emails
 * @author Peter Abelseth
 * @version 4
 *
 */
public class EmailManager {
	
	private static EmailManager singleton = null;
	
	private Properties props = null;
	
	//Parameters for email server
	private String emailHost;
	private String emailFrom;
	private String emailPassword;
	private String emailPort;
	
	/**
	 * Private constructor
	 */
	private EmailManager(){
	}
	
	/**
	 * Configures the email manager with the given settings
	 * @param emailHost The host of the email server
	 * @param emailFrom The email account
	 * @param emailPassword The password of the account
	 * @param emailPort The port of the email server
	 * @return The single EmailManager
	 */
	public static synchronized EmailManager configureEmailManager(String emailHost, String emailFrom, String emailPassword, String emailPort){
		singleton = new EmailManager();
		
		singleton.emailHost = emailHost;
		singleton.emailFrom = emailFrom;
		singleton.emailPassword = emailPassword;
		singleton.emailPort = emailPort;
		singleton.configureEmailServer();
		
		try{
			singleton.verifyConnection();
		} catch(MessagingException e){
			ErrorManager.getReference().fatalSubsystemError(e, singleton);
			singleton = null;
		}
		return singleton;
	}
	
	/**
	 * Returns the reference to the single EmailManager object
	 * 
	 * @return The reference to the EmailManager
	 */
	public static synchronized EmailManager getReference(){
		if(singleton == null)
			ErrorManager.getReference().fatalSubsystemError(new InstantiationException("Email Manager hasn't be initialized."), singleton);
		return singleton;
	}
	
	/**
	 * Closes EmailManager, allows it to be picked up by garbage collector
	 */
	public static synchronized void close(){
		if(singleton != null && singleton.props != null)
			singleton.props.clear();
		singleton = null;
	}
	
	
	/**
	 * Sends the given email using the settings specified earlier
	 * 
	 * @param emailAddress The address to send to
	 * @param emailSubject The subject of the email
	 * @param emailMessage The body of the message
	 * @throws MessagingException If the message failed to be sent
	 */
	public void sendEmail(String emailAddress, String emailSubject, String emailMessage)throws MessagingException{
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator(){
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(emailFrom,emailPassword);
					}
				});
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(emailFrom));
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailAddress));
		message.setSubject(emailSubject);
		message.setText(emailMessage);
		Transport.send(message);
	}
	
	/**
	 * Configure the properties of the mail server
	 */
	private void configureEmailServer(){
		props = System.getProperties();
		props.put("mail.smtp.host", emailHost);
		props.put("mail.smtp.socketFactory.port", emailPort);
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.port", emailPort);
	}
	
	/**
	 * Verify that the given account information is accurate
	 * 
	 * @throws MessagingException If wasn't able to connect to the email server.
	 */
	private void verifyConnection() throws MessagingException{
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator(){
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(emailFrom,emailPassword);
					}
				});
		
		Transport transport = null;
		try{
			transport = session.getTransport("smtp");
			transport.connect();
		} catch(MessagingException e){
			throw e;
		} finally{
			if(transport != null)
				transport.close();
		}
	}
}
