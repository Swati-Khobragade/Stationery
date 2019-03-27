package com.gl.adminportal.stationery.service;

import java.util.Date;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gl.adminportal.stationery.model.EmpFeedback;
import com.gl.adminportal.stationery.utils.Constants;

@Component
public class EmailService {

	private static final Logger LOGGER = Logger.getLogger(EmailService.class);

	@Autowired
	private Message emailMessage;

	@Value("${emp.feedback.emailSubject}")
	private String empFeedbackEmailSubject;

	@Value("${emp.feedback.emailBody}")
	private String empFeedbackEmailBody;

	@Value("${feedback.mail.to}")
	private String feedbackMailTo;

	/**
	 * send mail to help desk when any feed back is submitted by employee
	 */
	public void sendFeedbackNotifiction(EmpFeedback empFeedback) {
		LOGGER.debug("Invoking sendFeedbackNotifiction method");

		try {
			emailMessage.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(feedbackMailTo));
			emailMessage.setRecipients(Message.RecipientType.CC, null);
			emailMessage.setSentDate(new Date());
			emailMessage.setSubject(empFeedbackEmailSubject);
			BodyPart messageBodyPart = new MimeBodyPart();
			String mailBody = empFeedbackEmailBody;
			messageBodyPart.setText(replaceFeedback(empFeedback, mailBody));
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			emailMessage.setContent(multipart);
			Transport.send(emailMessage);
		} catch (Exception e) {
			LOGGER.error("Exception parsing mail address");
			e.printStackTrace();
		}
	}

	private String replaceFeedback(EmpFeedback feedback, String mailBody) {
		return mailBody.replace(Constants.EMP_ID, feedback.getEmpId())
				.replace(Constants.EMP_NAME, feedback.getEmpName())
				.replace(Constants.EMP_MAIL, feedback.getEmail())
				.replace(Constants.EMP_FEEDBACK, feedback.getFeedback());
	}
}
