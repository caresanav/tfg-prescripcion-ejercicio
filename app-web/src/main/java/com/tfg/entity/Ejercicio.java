package com.tfg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Definición de la tabla ejercicio
@Entity
@Table(name = "ejercicio")
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    private String nivel;

    private String material;

    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "tipo_ejercicio_id")
    private TipoEjercicio tipoEjercicio;

    public Ejercicio() {
    }

    public Ejercicio(String nombre, String descripcion, String nivel, String material, Boolean activo, TipoEjercicio tipoEjercicio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.material = material;
        this.activo = activo;
        this.tipoEjercicio = tipoEjercicio;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNivel() {
        return nivel;
    }

    public String getMaterial() {
        return material;
    }

    public Boolean getActivo() {
        return activo;
    }

    public TipoEjercicio getTipoEjercicio() {
        return tipoEjercicio;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setTipoEjercicio(TipoEjercicio tipoEjercicio) {
        this.tipoEjercicio = tipoEjercicio;
    }
}