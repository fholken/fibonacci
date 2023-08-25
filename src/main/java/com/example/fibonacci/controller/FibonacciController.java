package com.example.fibonacci.controller;

import com.example.fibonacci.service.EmailService;
import com.example.fibonacci.service.FibonacciService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@RestController
public class FibonacciController {

    private final FibonacciService fibonacciService;
    private final EmailService emailService;

    @Value("${basic.auth.username}")
    private String basicAuthUsername;

    @Value("${basic.auth.password}")
    private String basicAuthPassword;

    public FibonacciController(FibonacciService fibonacciService, EmailService emailService) {
        this.fibonacciService = fibonacciService;
        this.emailService = emailService;
    }

    @GetMapping("/send-email")
    public ResponseEntity<String> sendFibonacciEmail(@RequestHeader("Authorization") String authHeader) {
        // Verificar la autenticación básica
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso no autorizado.");
        }

        List<Long> fibonacciNumbers = fibonacciService.generateFibonacciSeries();
        List<String> recipientEmails = Arrays.asList("mauro.g.rivera@gmail.com", "didier.correa@proteccion.com.co", "correalondon@gmail.com");

        try {
            emailService.sendFibonacciEmail(recipientEmails, fibonacciNumbers);
            return ResponseEntity.ok("Correo enviado con éxito a los destinatarios.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo: " + e.getMessage());
        }
    }

    private boolean isAuthorized(String authHeader) {
        // Extraer y decodificar las credenciales
        String base64Credentials = authHeader.substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));

        // Dividir las credenciales en usuario y contraseña
        String[] splitCredentials = credentials.split(":");
        String providedUsername = splitCredentials[0];
        String providedPassword = splitCredentials[1];

        // Verificar las credenciales con las propiedades configuradas
        return basicAuthUsername.equals(providedUsername) && basicAuthPassword.equals(providedPassword);
    }
}
