package com.example.fibonacci.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendFibonacciEmail(List<String> recipientEmails, List<Long> fibonacciNumbers) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String subject = "Prueba Tecnica - Mauricio Gomez Rivera";
        String body = "La secuencia de Fibonacci generada es: " + fibonacciNumbers.toString();

        for (String recipientEmail : recipientEmails) {
            helper.addTo(recipientEmail);
        }
        helper.setSubject(subject);
        helper.setFrom(senderEmail);
        helper.setText(body, true);

        javaMailSender.send(message);
    }
}
