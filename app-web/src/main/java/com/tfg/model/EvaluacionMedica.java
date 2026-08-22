package com.tfg.model;

// Creada por Carmen Esau
// Objeto de datos para la evaluación médica realizada por el médico

//import java.io.Serializable;
import com.tfg.model.enums.NivelActividad;
import com.tfg.model.enums.RiesgoCardiovascular;
import com.tfg.model.enums.ObjetivoPrincipal;

// Modelo java idéntico al del proceso

public class EvaluacionMedica {

    // Datos paciente
    private String nombre;
    private String apellidos;
    private Integer edad;
    private String patologias;
    private String email;
    private Boolean necesitaRevisionFisioterapeuta;

    // Datos clinicos
    private Double pesoKg;
    private Double alturaCm;
    private String tensionArterial; // ej: "120/80"
    private Integer frecuenciaCardiacaReposo; // bpm
    private String medicacionActual;
    private NivelActividad nivelActividad;

    // Riesgo y restricciones
    private RiesgoCardiovascular riesgoCardiovascular; // "Bajo", "Medio", "Alto"
    private String contraindicaciones; // ej: "evitar impacto"
    private Integer dolorActual; // 0-10
    private String zonaDolor; // ej: "rodilla derecha"
    private ObjetivoPrincipal objetivoPrincipal; // ej: "rehabilitación", "pérdida de peso"

    // Getters y setters (necesarios para que Spring/Thymeleaf puedan leer/escribir
    // los campos)

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getPatologias() {
        return patologias;
    }

    public void setPatologias(String patologias) {
        this.patologias = patologias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getNecesitaRevisionFisioterapeuta() {
        return necesitaRevisionFisioterapeuta;
    }

    public void setNecesitaRevisionFisioterapeuta(Boolean necesitaRevisionFisioterapeuta) {
        this.necesitaRevisionFisioterapeuta = necesitaRevisionFisioterapeuta;
    }

    public Double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(Double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public Double getAlturaCm() {
        return alturaCm;
    }

    public void setAlturaCm(Double alturaCm) {
        this.alturaCm = alturaCm;
    }

    public String getTensionArterial() {
        return tensionArterial;
    }

    public void setTensionArterial(String tensionArterial) {
        this.tensionArterial = tensionArterial;
    }

    public Integer getFrecuenciaCardiacaReposo() {
        return frecuenciaCardiacaReposo;
    }

    public void setFrecuenciaCardiacaReposo(Integer frecuenciaCardiacaReposo) {
        this.frecuenciaCardiacaReposo = frecuenciaCardiacaReposo;
    }

    public String getMedicacionActual() {
        return medicacionActual;
    }

    public void setMedicacionActual(String medicacionActual) {
        this.medicacionActual = medicacionActual;
    }

    public NivelActividad getNivelActividad() {
        return nivelActividad;
    }

    public void setNivelActividad(NivelActividad nivelActividad) {
        this.nivelActividad = nivelActividad;
    }

    public RiesgoCardiovascular getRiesgoCardiovascular() {
        return riesgoCardiovascular;
    }

    public void setRiesgoCardiovascular(RiesgoCardiovascular riesgoCardiovascular) {
        this.riesgoCardiovascular = riesgoCardiovascular;
    }

    public String getContraindicaciones() {
        return contraindicaciones;
    }

    public void setContraindicaciones(String contraindicaciones) {
        this.contraindicaciones = contraindicaciones;
    }

    public Integer getDolorActual() {
        return dolorActual;
    }

    public void setDolorActual(Integer dolorActual) {
        this.dolorActual = dolorActual;
    }

    public String getZonaDolor() {
        return zonaDolor;
    }

    public void setZonaDolor(String zonaDolor) {
        this.zonaDolor = zonaDolor;
    }

    public ObjetivoPrincipal getObjetivoPrincipal() {
        return objetivoPrincipal;
    }

    public void setObjetivoPrincipal(ObjetivoPrincipal objetivoPrincipal) {
        this.objetivoPrincipal = objetivoPrincipal;
    }

}
