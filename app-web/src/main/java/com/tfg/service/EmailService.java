package com.tfg.service;

import com.tfg.model.CitaEmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Servicio que envía correos reales utilizando la configuración SMTP de Spring Boot

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    // Dirección remitente (la sacamos de application.properties)
    @Value("${spring.mail.from}")
    private String from;

    @Value("${app.base-url}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreoCita(CitaEmailRequest request) {
        // Construimos el mensaje de correo
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // desde
        message.setTo(request.getEmail()); // a quién
        message.setSubject("Cita de revisión para fisioterapia");

        String enlaceConfirmar = baseUrl + "/respuesta/confirmar?token=" + request.getTokenRespuesta();
        String enlaceNuevaCita = baseUrl + "/respuesta/nueva-cita?token=" + request.getTokenRespuesta();

        // Cuerpo del mensaje (texto plano)
        String texto = "Hola " + request.getNombre() + " " + request.getApellidos() + ",\n\n"
                //+ /*request.getMensaje() +*/ "\n\n"
                + "Has sido asignado una cita para revisión con fisioterapia. Puedes responder usando uno de estos enlaces:\n\n"
                + "Confirmar cita:\n"
                + enlaceConfirmar + "\n\n"
                + "Solicitar nueva cita:\n"
                + enlaceNuevaCita + "\n\n"
                + "Un saludo,\n"
                + "Servicio de Prescripción de Ejercicio";

        message.setText(texto);

        // Envío real del correo
        mailSender.send(message);

    }
}