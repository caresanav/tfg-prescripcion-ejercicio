package com.tfg.web;

import com.tfg.entity.EmailEnvio;
import com.tfg.service.EmailEnvioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailRespuestaController {

    private final EmailEnvioService emailEnvioService;

    public EmailRespuestaController(EmailEnvioService emailEnvioService) {
        this.emailEnvioService = emailEnvioService;
    }

    @GetMapping("/respuesta/confirmar")
    public String confirmar(@RequestParam String token) {
        EmailEnvio emailEnvio = emailEnvioService.confirmarRespuesta(token);

        return "Cita confirmada para "
                + emailEnvio.getNombrePaciente()
                + " "
                + emailEnvio.getApellidosPaciente()
                + ".";
    }

    @GetMapping("/respuesta/nueva-cita")
    public String solicitarNuevaCita(@RequestParam String token) {
        EmailEnvio emailEnvio = emailEnvioService.solicitarNuevaCita(token);

        return "Se ha solicitado una nueva cita para "
                + emailEnvio.getNombrePaciente()
                + " "
                + emailEnvio.getApellidosPaciente()
                + ".";
    }
}