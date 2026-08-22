package com.tfg.client.dto;

import java.time.LocalDateTime;

public record ResumenResponse(
        Long id,
        Long processInstanceId,
        String nombrePaciente,
        String apellidosPaciente,
        String emailPaciente,
        String nombrePlan,
        Integer duracionSemanas,
        Integer frecuenciaSemanal,
        String indicacionesGenerales,
        String observacionesEntrenador,
        String ejerciciosSeleccionados,
        String tokenAcceso,
        LocalDateTime fechaCreacion
) {
}