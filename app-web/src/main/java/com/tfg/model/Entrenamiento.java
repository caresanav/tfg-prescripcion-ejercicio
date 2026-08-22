package com.tfg.model;

public class Entrenamiento {

    private String nombrePlan;
    private Integer duracionSemanas;
    private Integer frecuenciaSemanal;
    private String ejerciciosSeleccionados;
    private String indicacionesGenerales;
    private String observacionesEntrenador;

    public Entrenamiento() {
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

    public String getEjerciciosSeleccionados() {
        return ejerciciosSeleccionados;
    }

    public void setEjerciciosSeleccionados(String ejerciciosSeleccionados) {
        this.ejerciciosSeleccionados = ejerciciosSeleccionados;
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
}