package com.tfg.resumenapi.dto;

public record CrearResumenRequest(
        Long processInstanceId,
        String nombrePaciente,
        String apellidosPaciente,
        String emailPaciente,
        String nombrePlan,
        Integer duracionSemanas,
        Integer frecuenciaSemanal,
        String indicacionesGenerales,
        String observacionesEntrenador,
        String ejerciciosSeleccionados
) {
}