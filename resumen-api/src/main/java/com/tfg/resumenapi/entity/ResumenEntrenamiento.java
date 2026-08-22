package com.tfg.resumenapi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entidad para el resumen del entrenamiento (lo que verá el paciente)

@Entity
@Table(name = "resumen_entrenamiento")
public class ResumenEntrenamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long processInstanceId;

    private String nombrePaciente;

    private String apellidosPaciente;

    private String emailPaciente;

    private String nombrePlan;

    private Integer duracionSemanas;

    private Integer frecuenciaSemanal;

    @Column(length = 3000)
    private String indicacionesGenerales;

    @Column(length = 3000)
    private String observacionesEntrenador;

    @Column(length = 3000)
    private String ejerciciosSeleccionados;

    @Column(unique = true, length = 255)
    private String tokenAcceso;

    private LocalDateTime fechaCreacion;

    public ResumenEntrenamiento() {
    }

    public Long getId() {
        return id;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getApellidosPaciente() {
        return apellidosPaciente;
    }

    public void setApellidosPaciente(String apellidosPaciente) {
        this.apellidosPaciente = apellidosPaciente;
    }

    public String getEmailPaciente() {
        return emailPaciente;
    }

    public void setEmailPaciente(String emailPaciente) {
        this.emailPaciente = emailPaciente;
    }

    public String getNombrePlan() {
        return nombrePlan;
    }

    public void setNombrePlan(String nombrePlan) {
        this.nombrePlan = nombrePlan;
    }

    public Integer getDuracionSemanas() {
        return duracionSemanas;
    }

    public void setDuracionSemanas(Integer duracionSemanas) {
        this.duracionSemanas = duracionSemanas;
    }

    public Integer getFrecuenciaSemanal() {
        return frecuenciaSemanal;
    }

    public void setFrecuenciaSemanal(Integer frecuenciaSemanal) {
        this.frecuenciaSemanal = frecuenciaSemanal;
    }

    public String getIndicacionesGenerales() {
        return indicacionesGenerales;
    }

    public void setIndicacionesGenerales(String indicacionesGenerales) {
        this.indicacionesGenerales = indicacionesGenerales;
    }

    public String getObservacionesEntrenador() {
        return observacionesEntrenador;
    }

    public void setObservacionesEntrenador(String observacionesEntrenador) {
        this.observacionesEntrenador = observacionesEntrenador;
    }

    public String getEjerciciosSeleccionados() {
        return ejerciciosSeleccionados;
    }

    public void setEjerciciosSeleccionados(String ejerciciosSeleccionados) {
        this.ejerciciosSeleccionados = ejerciciosSeleccionados;
    }

    public String getTokenAcceso() {
        return tokenAcceso;
    }

    public void setTokenAcceso(String tokenAcceso) {
        this.tokenAcceso = tokenAcceso;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}