package com.omarahmed42.ecommerce.event;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.service.VerificationTokenService;

@Component
public class RegistrationListener {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private JavaMailSender mailSender;

    private static final String SENDER = "no-reply@ecommerce.com";

    @EventListener
    @Async
    public void onApplicationEvent(OnRegistrationEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationEvent event) {
        User user = event.getUser();
        UUID token = verificationTokenService.addVerificationToken(user);

        String recipientEmail = user.getEmail();
        String subject = "Verify your email";
        String confirmationURL = "http://localhost:8080/confirm?token=" + token.toString();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(SENDER);
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText("Click the following link to verify: \r\n" + confirmationURL);

        mailSender.send(mailMessage);
    }
}
