package com.gl.adminportal.stationery.configuration;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.gl.adminportal.stationery.utils.Constants;

@Configuration
@ComponentScan({ "com.gl.adminportal.stationery.configuration" })
//@PropertySource(value = { "file:${FOODZILLA_CONFIG}stationeryMangmt.properties" })
@PropertySource("classpath:stationeryMangmt.properties")
public class EmailConfiguration {

	private static final Logger LOGGER = Logger.getLogger(EmailConfiguration.class);

	@Autowired
	private Environment environment;
	
	@Bean
	public Message emailMessage() {
		Message message = null;
		try {
			// sets SMTP server properties
			Properties properties = new Properties();
			properties.put(Constants.EMAIL_HOST, environment.getRequiredProperty("email.host"));
			properties.put(Constants.EMAIL_PORT, environment.getRequiredProperty("email.port"));
			properties.put(Constants.EMAIL_STARTTLS_ENABLE, "true");
			final String userName = environment.getRequiredProperty("email.userName");
			final String password = environment.getRequiredProperty("email.password");
			
			Session session = null;
			// creates a new session with an authenticator
			if (password != null && !password.isEmpty()) {
				Authenticator auth = new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(userName, password);
					}
				};
				properties.put(Constants.EMAIL_AUTH, "true");
				session = Session.getInstance(properties, auth);
			} else {
				session = Session.getInstance(properties);
			}
		//	LOGGER.info("Recipients of mail : to "+toAddress + " cc "+ccAddress);
			// creates a new e-mail message
			message = new MimeMessage(session);
			message.setFrom(new InternetAddress(userName));
			/*message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
			LOGGER.info("Mail sending to : "+toAddress + " cc : "+ccAddress);
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccAddress));*/
		} catch (AddressException e) {
			LOGGER.error("Exception while configuring the email settings.", e);
		} catch (MessagingException e) {
			LOGGER.error("Exception while configuring the email settings.", e);
		}
		return message;
	}

}
