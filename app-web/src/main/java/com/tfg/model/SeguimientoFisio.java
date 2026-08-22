package com.tfg.model;

public class SeguimientoFisio {

    private Integer dolorActual;
    private Integer adherenciaPorcentaje;
    private String evolucion;
    private String incidencias;
    private String observaciones;
    private Boolean requiereSeguimientoMedico;
    private Boolean cambiaPrescripcion;

    public SeguimientoFisio() {
    }

    public Integer getDolorActual() {
        return dolorActual;
    }

    public void setDolorActual(Integer dolorActual) {
        this.dolorActual = dolorActual;
    }

    public Integer getAdherenciaPorcentaje() {
        return adherenciaPorcentaje;
    }

    public void setAdherenciaPorcentaje(Integer adherenciaPorcentaje) {
        this.adherenciaPorcentaje = adherenciaPorcentaje;
    }

    public String getEvolucion() {
        return evolucion;
    }

    public void setEvolucion(String evolucion) {
        this.evolucion = evolucion;
    }

    public String getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(String incidencias) {
        this.incidencias = incidencias;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getRequiereSeguimientoMedico() {
        return requiereSeguimientoMedico;
    }

    public void setRequiereSeguimientoMedico(
            Boolean requiereSeguimientoMedico) {
        this.requiereSeguimientoMedico = requiereSeguimientoMedico;
    }

    public Boolean getCambiaPrescripcion() {
        return cambiaPrescripcion;
    }

    public void setCambiaPrescripcion(Boolean cambiaPrescripcion) {
        this.cambiaPrescripcion = cambiaPrescripcion;
    }
}