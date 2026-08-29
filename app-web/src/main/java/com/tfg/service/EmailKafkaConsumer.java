package com.tfg.service;

import com.tfg.model.CitaEmailRequest;
import com.tfg.model.EventoEmail;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmailKafkaConsumer {

    private final EmailService emailService;
    private final EmailEnvioService emailEnvioService;

    public EmailKafkaConsumer(EmailService emailService, EmailEnvioService emailEnvioService) {
        this.emailService = emailService;
        this.emailEnvioService = emailEnvioService;
    }

    // Escuchamos al topic emails
    @KafkaListener(topics = "emails")
    public void consumir(EventoEmail event) {
        // Convertimos EventoEmail a CitaEmailRequest porque es lo que manda
        // EmailService
        CitaEmailRequest req = new CitaEmailRequest();
        req.setNombre(event.getNombre());
        req.setApellidos(event.getApellidos());
        req.setEmail(event.getEmail());
        req.setTokenRespuesta(event.getTokenRespuesta());

        try {
            emailService.enviarCorreoCita(req);

            if (event.getEmailEnvioId() != null) {
                emailEnvioService.marcarEnviado(event.getEmailEnvioId());
            }

            System.out.println("Email enviado correctamente mediante Kafka");

        } catch (Exception e) {

            if (event.getEmailEnvioId() != null) {
                emailEnvioService.marcarError(event.getEmailEnvioId(), e.getMessage());
            }

            System.err.println("Error al enviar email mediante Kafka: "+ e.getMessage());
            throw e;
        }
    }
}