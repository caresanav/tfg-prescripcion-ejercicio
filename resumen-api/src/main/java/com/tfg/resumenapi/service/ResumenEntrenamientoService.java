package com.tfg.resumenapi.service;

import com.tfg.resumenapi.dto.CrearResumenRequest;
import com.tfg.resumenapi.dto.ResumenResponse;
import com.tfg.resumenapi.entity.ResumenEntrenamiento;
import com.tfg.resumenapi.repository.ResumenEntrenamientoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ResumenEntrenamientoService {

    private final ResumenEntrenamientoRepository resumenRepository;

    public ResumenEntrenamientoService(
            ResumenEntrenamientoRepository resumenRepository) {
        this.resumenRepository = resumenRepository;
    }

    /*
     * Crea y guarda un nuevo resumen en PostgreSQL.
     */
    @Transactional
    public ResumenResponse crear(CrearResumenRequest request) {

        ResumenEntrenamiento resumen =
                new ResumenEntrenamiento();

        resumen.setProcessInstanceId(
                request.processInstanceId());

        resumen.setNombrePaciente(
                request.nombrePaciente());

        resumen.setApellidosPaciente(
                request.apellidosPaciente());

        resumen.setEmailPaciente(
                request.emailPaciente());

        resumen.setNombrePlan(
                request.nombrePlan());

        resumen.setDuracionSemanas(
                request.duracionSemanas());

        resumen.setFrecuenciaSemanal(
                request.frecuenciaSemanal());

        resumen.setIndicacionesGenerales(
                request.indicacionesGenerales());

        resumen.setObservacionesEntrenador(
                request.observacionesEntrenador());

        resumen.setEjerciciosSeleccionados(
                request.ejerciciosSeleccionados());

        // El token se genera en la API, no en app-web.
        resumen.setTokenAcceso(
                UUID.randomUUID().toString());

        resumen.setFechaCreacion(
                LocalDateTime.now());

        ResumenEntrenamiento resumenGuardado =
                resumenRepository.save(resumen);

        return convertirAResponse(resumenGuardado);
    }

    /*
     * Busca un resumen utilizando el token público.
     */
    @Transactional(readOnly = true)
    public ResumenResponse buscarPorToken(String token) {

        ResumenEntrenamiento resumen =
                resumenRepository
                        .findByTokenAcceso(token)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "No existe ningún resumen con ese token"
                                )
                        );

        return convertirAResponse(resumen);
    }

    /*
     * Convierte la entidad de base de datos en el DTO
     * que devolverá la API como JSON.
     */
    private ResumenResponse convertirAResponse(
            ResumenEntrenamiento resumen) {

        return new ResumenResponse(
                resumen.getId(),
                resumen.getProcessInstanceId(),
                resumen.getNombrePaciente(),
                resumen.getApellidosPaciente(),
                resumen.getEmailPaciente(),
                resumen.getNombrePlan(),
                resumen.getDuracionSemanas(),
                resumen.getFrecuenciaSemanal(),
                resumen.getIndicacionesGenerales(),
                resumen.getObservacionesEntrenador(),
                resumen.getEjerciciosSeleccionados(),
                resumen.getTokenAcceso(),
                resumen.getFechaCreacion()
        );
    }
}