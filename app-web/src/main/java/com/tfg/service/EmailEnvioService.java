package com.tfg.service;

import com.tfg.entity.EmailEnvio;
import com.tfg.repository.EmailEnvioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailEnvioService {

    private final EmailEnvioRepository emailEnvioRepository;

    public EmailEnvioService(EmailEnvioRepository emailEnvioRepository) {
        this.emailEnvioRepository = emailEnvioRepository;
    }

    public EmailEnvio guardar(EmailEnvio emailEnvio) {
        return emailEnvioRepository.save(emailEnvio);
    }

    public EmailEnvio guardarPendiente(Long processInstanceId,
            String nombrePaciente,
            String apellidosPaciente,
            String emailDestino) {

        EmailEnvio emailEnvio = new EmailEnvio();
        emailEnvio.setProcessInstanceId(processInstanceId);
        emailEnvio.setNombrePaciente(nombrePaciente);
        emailEnvio.setApellidosPaciente(apellidosPaciente);
        emailEnvio.setEmailDestino(emailDestino);
        emailEnvio.setEstadoEnvio("PENDIENTE");
        emailEnvio.setEstadoRespuesta(null);
        emailEnvio.setTokenRespuesta(UUID.randomUUID().toString());
        emailEnvio.setFechaCreacion(LocalDateTime.now());

        return emailEnvioRepository.save(emailEnvio);
    }

    public EmailEnvio marcarEnviado(Long emailEnvioId) {
        EmailEnvio emailEnvio = obtenerPorId(emailEnvioId);
        emailEnvio.setEstadoEnvio("ENVIADO");
        emailEnvio.setFechaEnvio(LocalDateTime.now());
        emailEnvio.setErrorDetalle(null);

        if (emailEnvio.getEstadoRespuesta() == null) {
            emailEnvio.setEstadoRespuesta("PENDIENTE_RESPUESTA");
        }

        return emailEnvioRepository.save(emailEnvio);
    }

    public EmailEnvio marcarError(Long emailEnvioId, String errorDetalle) {
        EmailEnvio emailEnvio = obtenerPorId(emailEnvioId);
        emailEnvio.setEstadoEnvio("ERROR");
        emailEnvio.setErrorDetalle(recortarError(errorDetalle));
        return emailEnvioRepository.save(emailEnvio);
    }

    public EmailEnvio confirmarRespuesta(String tokenRespuesta) {
        EmailEnvio emailEnvio = obtenerPorToken(tokenRespuesta);
        emailEnvio.setEstadoRespuesta("CONFIRMADO");
        emailEnvio.setFechaRespuesta(LocalDateTime.now());
        return emailEnvioRepository.save(emailEnvio);
    }

    public EmailEnvio solicitarNuevaCita(String tokenRespuesta) {
        EmailEnvio emailEnvio = obtenerPorToken(tokenRespuesta);
        emailEnvio.setEstadoRespuesta("SOLICITA_NUEVA_CITA");
        emailEnvio.setFechaRespuesta(LocalDateTime.now());
        return emailEnvioRepository.save(emailEnvio);
    }

    // Solo true cuando el paciente ha confirmado

    public boolean pacienteHaConfirmadoRevisionFisica(Long processInstanceId) {
        if (processInstanceId == null) {
            return false;
        }

        return emailEnvioRepository
                .findFirstByProcessInstanceIdOrderByFechaCreacionDesc(processInstanceId)
                .map(emailEnvio -> "CONFIRMADO".equals(emailEnvio.getEstadoRespuesta()))
                .orElse(false);
    }

    private EmailEnvio obtenerPorId(Long emailEnvioId) {
        return emailEnvioRepository.findById(emailEnvioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe EmailEnvio con id = " + emailEnvioId));
    }

    private EmailEnvio obtenerPorToken(String tokenRespuesta) {
        return emailEnvioRepository.findByTokenRespuesta(tokenRespuesta)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe EmailEnvio con tokenRespuesta = " + tokenRespuesta));
    }

    private String recortarError(String errorDetalle) {
        if (errorDetalle == null) {
            return null;
        }
        return errorDetalle.length() > 1000
                ? errorDetalle.substring(0, 1000)
                : errorDetalle;
    }
}