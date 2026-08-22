package com.tfg.model;

public class SeguimientoMedico {

    private String evolucionClinica;
    private String nuevasContraindicaciones;
    private String observaciones;
    private Boolean necesitaContinuar;
    private Boolean cambiaPrescripcion;

    public SeguimientoMedico() {
    }

    public String getEvolucionClinica() {
        return evolucionClinica;
    }

    public void setEvolucionClinica(String evolucionClinica) {
        this.evolucionClinica = evolucionClinica;
    }

    public String getNuevasContraindicaciones() {
        return nuevasContraindicaciones;
    }

    public void setNuevasContraindicaciones(
            String nuevasContraindicaciones) {
        this.nuevasContraindicaciones = nuevasContraindicaciones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getNecesitaContinuar() {
        return necesitaContinuar;
    }

    public void setNecesitaContinuar(Boolean necesitaContinuar) {
        this.necesitaContinuar = necesitaContinuar;
    }

    public Boolean getCambiaPrescripcion() {
        return cambiaPrescripcion;
    }

    public void setCambiaPrescripcion(Boolean cambiaPrescripcion) {
        this.cambiaPrescripcion = cambiaPrescripcion;
    }
}