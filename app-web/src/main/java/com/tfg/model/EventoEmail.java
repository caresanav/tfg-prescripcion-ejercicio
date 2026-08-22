package com.tfg.model;

// Objeto que mando a Kafka

public class EventoEmail {
    private String nombre;
    private String apellidos;
    private String email;
    private String mensaje;
    private Long emailEnvioId;
    private String tokenRespuesta;

    public EventoEmail() {
    }

    // getters y setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getEmailEnvioId() {
        return emailEnvioId;
    }

    public void setEmailEnvioId(Long emailEnvioId) {
        this.emailEnvioId = emailEnvioId;
    }

    public String getTokenRespuesta() {
        return tokenRespuesta;
    }

    public void setTokenRespuesta(String tokenRespuesta) {
        this.tokenRespuesta = tokenRespuesta;
    }
}
