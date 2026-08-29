package com.tfg.model;

// Creada por Carmen Esau
// Representa los datos mínimos para enviar un correo de cita

public class CitaEmailRequest {

    private String nombre;
    private String apellidos;
    private String email;
    //private String mensaje;
    private String tokenRespuesta;

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

    /*public String getMensaje() {
        return mensaje;
    }*/

    /*public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }*/

    public String getTokenRespuesta() {
        return tokenRespuesta;
    }

    public void setTokenRespuesta(String tokenRespuesta) {
        this.tokenRespuesta = tokenRespuesta;
    }
}
