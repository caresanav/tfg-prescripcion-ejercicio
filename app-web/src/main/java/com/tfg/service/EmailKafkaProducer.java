package com.tfg.service;

// Servicio que publica mensaje en Kafka

import com.tfg.model.EventoEmail;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailKafkaProducer {

    private final KafkaTemplate<String, EventoEmail> kafkaTemplate;

    public EmailKafkaProducer(KafkaTemplate<String, EventoEmail> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarEmail(EventoEmail event) {
        // Publicamos en topic emails
        // Se convierte a JSON automaticamente (configurado en application.properties)
        kafkaTemplate.send("emails", event);
    }
}
