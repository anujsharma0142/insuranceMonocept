package com.insurance.monocept.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class MailUtility {
	
	@Autowired
	private JavaMailSender javaMailSender;
	 
    @Value
    ("${spring.mail.username}") 
    private String sender;
 
    // Method 1
    // To send a simple email
    public String sendSimpleMail(String receiverEmail, String message, String subject)
    {
 
        // Try block to check for exceptions
        try {
 
            // Creating a simple mail message
            SimpleMailMessage mailMessage
                = new SimpleMailMessage();
 
            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(receiverEmail);
            mailMessage.setText(message);
            mailMessage.setSubject(subject);
 
            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }
 
        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }
}
