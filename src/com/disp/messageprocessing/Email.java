package com.disp.messageprocessing;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

	public void SendMyEmail (final String sender , final String password, String reciver, String body){

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("rami.torkhani@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(reciver));
			message.setSubject("New Signalement");
			message.setText(body);

			Transport.send(message);

			System.out.println("Done ... Your email was sent ! ");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}